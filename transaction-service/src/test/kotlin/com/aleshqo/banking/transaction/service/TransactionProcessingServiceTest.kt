package com.aleshqo.banking.transaction.service

import com.aleshqo.banking.common.dto.BalanceConfirmRequest
import com.aleshqo.banking.common.dto.BalanceReserveRequest
import com.aleshqo.banking.common.enums.TransactionStatus
import com.aleshqo.banking.common.event.TransactionCancelEvent
import com.aleshqo.banking.common.event.TransactionConfirmEvent
import com.aleshqo.banking.common.event.TransactionTryEvent
import com.aleshqo.banking.transaction.client.AccountClient
import com.aleshqo.banking.transaction.entity.Transaction
import com.aleshqo.banking.transaction.exception.TransactionNotFoundException
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant

class TransactionProcessingServiceTest {

    private val accountClient: AccountClient = mockk()
    private val persistence: TransactionPersistence = mockk(relaxed = true)
    private val service = TransactionProcessingService(accountClient, persistence)

    @Test
    fun `try transaction reserves amount and enqueues confirm`() {
        val event = tryEvent("req-1")
        every { persistence.exists("req-1") } returns false
        every { persistence.createPending(event) } returns pendingTransaction("req-1")
        every { accountClient.reserveAmount(any<BalanceReserveRequest>()) } just runs
        every { persistence.enqueueConfirm(event) } just runs

        service.tryTransaction(event)

        verify(exactly = 1) { persistence.createPending(event) }
        verify(exactly = 1) { accountClient.reserveAmount(any<BalanceReserveRequest>()) }
        verify(exactly = 1) { persistence.enqueueConfirm(event) }
    }

    @Test
    fun `try transaction marks canceled when reserve fails`() {
        val event = tryEvent("req-2")
        every { persistence.exists("req-2") } returns false
        every { persistence.createPending(event) } returns pendingTransaction("req-2")
        every { accountClient.reserveAmount(any<BalanceReserveRequest>()) } throws RuntimeException("reserve failed")
        every { persistence.markCanceled("req-2") } just runs
        every { persistence.enqueueCancel(event, any()) } just runs

        service.tryTransaction(event)

        verify(exactly = 1) { persistence.markCanceled("req-2") }
        verify(exactly = 1) { persistence.enqueueCancel(event, "reserve failed") }
    }

    @Test
    fun `confirm transaction marks confirmed`() {
        val event = confirmEvent("req-3")
        val transaction = pendingTransaction("req-3")
        every { persistence.findByRequestId("req-3") } returns transaction
        every { accountClient.confirmTransfer(any<BalanceConfirmRequest>()) } just runs
        every { persistence.markConfirmed(transaction, event) } just runs

        service.confirmTransaction(event)

        verify(exactly = 1) { persistence.markConfirmed(transaction, event) }
    }

    @Test
    fun `confirm transaction marks failed and enqueues cancel on error`() {
        val event = confirmEvent("req-4")
        val transaction = pendingTransaction("req-4")
        every { persistence.findByRequestId("req-4") } returns transaction
        every { accountClient.confirmTransfer(any<BalanceConfirmRequest>()) } throws RuntimeException("confirm failed")
        every { persistence.markFailed("req-4") } just runs
        every { persistence.enqueueCancelFromConfirm(event, "confirm failed") } just runs

        service.confirmTransaction(event)

        verify(exactly = 1) { persistence.markFailed("req-4") }
        verify(exactly = 1) { persistence.enqueueCancelFromConfirm(event, "confirm failed") }
    }

    @Test
    fun `confirm throws when transaction not found`() {
        every { persistence.findByRequestId("missing") } returns null

        assertThrows(TransactionNotFoundException::class.java) {
            service.confirmTransaction(confirmEvent("missing"))
        }
    }

    private fun tryEvent(requestId: String) = TransactionTryEvent(
        requestId = requestId,
        senderId = 1L,
        receiverId = 2L,
        amount = BigDecimal("15.00"),
        timestamp = Instant.now()
    )

    private fun confirmEvent(requestId: String) = TransactionConfirmEvent(
        requestId = requestId,
        senderId = 1L,
        receiverId = 2L,
        amount = BigDecimal("10.00"),
        timestamp = Instant.now()
    )

    private fun pendingTransaction(requestId: String) = Transaction(
        id = 1L,
        requestId = requestId,
        senderId = 1L,
        receiverId = 2L,
        amount = BigDecimal("10.00"),
        status = TransactionStatus.PENDING
    )
}
