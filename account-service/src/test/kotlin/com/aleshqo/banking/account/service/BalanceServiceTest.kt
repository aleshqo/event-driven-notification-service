package com.aleshqo.banking.account.service

import com.aleshqo.banking.account.entity.Account
import com.aleshqo.banking.account.entity.Reservation
import com.aleshqo.banking.account.exception.InsufficientFundsException
import com.aleshqo.banking.account.exception.InvalidReservationException
import com.aleshqo.banking.account.repository.AccountRepository
import com.aleshqo.banking.account.repository.AccountTransactionRepository
import com.aleshqo.banking.account.repository.ReservationRepository
import com.aleshqo.banking.common.dto.BalanceCancelRequest
import com.aleshqo.banking.common.dto.BalanceConfirmRequest
import com.aleshqo.banking.common.dto.BalanceReserveRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.util.Optional

class BalanceServiceTest {

    private val accountRepository: AccountRepository = mock()
    private val reservationRepository: ReservationRepository = mock()
    private val accountTransactionRepository: AccountTransactionRepository = mock()
    private val service = BalanceService(accountRepository, reservationRepository, accountTransactionRepository)

    @Test
    fun `reserve moves money from available to reserved`() {
        val account = account(available = "100.00", reserved = "10.00")
        whenever(reservationRepository.existsById("req-1")).thenReturn(false)
        whenever(accountRepository.findById(1L)).thenReturn(Optional.of(account))

        service.reserve(BalanceReserveRequest(1L, BigDecimal("30.00"), "req-1"))

        assertEquals(BigDecimal("70.00"), account.availableBalance)
        assertEquals(BigDecimal("40.00"), account.reservedBalance)
    }

    @Test
    fun `reserve throws when insufficient funds`() {
        val account = account(available = "10.00", reserved = "0.00")
        whenever(reservationRepository.existsById("req-2")).thenReturn(false)
        whenever(accountRepository.findById(1L)).thenReturn(Optional.of(account))

        assertThrows(InsufficientFundsException::class.java) {
            service.reserve(BalanceReserveRequest(1L, BigDecimal("30.00"), "req-2"))
        }
    }

    @Test
    fun `confirm rejects mismatched sender`() {
        val reservation = Reservation("req-3", accountId = 99L, amount = BigDecimal("10.00"))
        whenever(reservationRepository.findById("req-3")).thenReturn(Optional.of(reservation))

        assertThrows(InvalidReservationException::class.java) {
            service.confirm(BalanceConfirmRequest(1L, 2L, BigDecimal("10.00"), "req-3"))
        }
    }

    @Test
    fun `reserve is idempotent for existing request id`() {
        whenever(reservationRepository.existsById("dup-request")).thenReturn(true)

        service.reserve(BalanceReserveRequest(1L, BigDecimal("10.00"), "dup-request"))

        verify(accountRepository, never()).findById(any())
        verify(accountRepository, never()).save(any<Account>())
    }

    private fun account(available: String, reserved: String) = Account(
        id = 1L,
        number = "A-1",
        ownerName = "Alexey",
        availableBalance = BigDecimal(available),
        reservedBalance = BigDecimal(reserved)
    )
}
