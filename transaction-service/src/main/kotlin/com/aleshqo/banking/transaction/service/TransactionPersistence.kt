package com.aleshqo.banking.transaction.service

import com.aleshqo.banking.common.enums.TransactionStatus
import com.aleshqo.banking.common.event.TransactionCancelEvent
import com.aleshqo.banking.common.event.TransactionCompletedEvent
import com.aleshqo.banking.common.event.TransactionConfirmEvent
import com.aleshqo.banking.common.event.TransactionTryEvent
import com.aleshqo.banking.transaction.entity.Transaction
import com.aleshqo.banking.transaction.outbox.OutboxService
import com.aleshqo.banking.transaction.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class TransactionPersistence(
    private val transactionRepository: TransactionRepository,
    private val outboxService: OutboxService
) {

    fun exists(requestId: String): Boolean =
        transactionRepository.findByRequestId(requestId) != null

    fun findByRequestId(requestId: String): Transaction? =
        transactionRepository.findByRequestId(requestId)

    @Transactional
    fun createPending(event: TransactionTryEvent): Transaction =
        transactionRepository.save(
            Transaction(
                requestId = event.requestId,
                senderId = event.senderId,
                receiverId = event.receiverId,
                amount = event.amount,
                status = TransactionStatus.PENDING
            )
        )

    @Transactional
    fun enqueueConfirm(event: TransactionTryEvent) {
        outboxService.enqueue(
            topic = TOPIC_CONFIRM,
            messageKey = event.requestId,
            payload = TransactionConfirmEvent(
                requestId = event.requestId,
                senderId = event.senderId,
                receiverId = event.receiverId,
                amount = event.amount,
                timestamp = Instant.now()
            )
        )
    }

    @Transactional
    fun markCanceled(requestId: String) {
        val transaction = transactionRepository.findByRequestId(requestId) ?: return
        transaction.status = TransactionStatus.CANCELED
        transactionRepository.save(transaction)
    }

    @Transactional
    fun enqueueCancel(event: TransactionTryEvent, reason: String) {
        outboxService.enqueue(
            topic = TOPIC_CANCEL,
            messageKey = event.requestId,
            payload = TransactionCancelEvent(
                requestId = event.requestId,
                senderId = event.senderId,
                amount = event.amount,
                timestamp = Instant.now(),
                reason = reason
            )
        )
    }

    @Transactional
    fun enqueueCancelFromConfirm(event: TransactionConfirmEvent, reason: String) {
        outboxService.enqueue(
            topic = TOPIC_CANCEL,
            messageKey = event.requestId,
            payload = TransactionCancelEvent(
                requestId = event.requestId,
                senderId = event.senderId,
                amount = event.amount,
                timestamp = Instant.now(),
                reason = reason
            )
        )
    }

    @Transactional
    fun markConfirmed(transaction: Transaction, event: TransactionConfirmEvent) {
        transaction.status = TransactionStatus.CONFIRMED
        transactionRepository.save(transaction)
        outboxService.enqueue(
            topic = TOPIC_COMPLETED,
            messageKey = event.requestId,
            payload = TransactionCompletedEvent(
                requestId = event.requestId,
                senderId = event.senderId,
                receiverId = event.receiverId,
                amount = event.amount,
                status = TransactionStatus.CONFIRMED,
                timestamp = Instant.now()
            )
        )
    }

    @Transactional
    fun markFailed(requestId: String) {
        val transaction = transactionRepository.findByRequestId(requestId) ?: return
        transaction.status = TransactionStatus.FAILED
        transactionRepository.save(transaction)
    }

    companion object {
        const val TOPIC_CONFIRM = "transaction-confirm"
        const val TOPIC_CANCEL = "transaction-cancel"
        const val TOPIC_COMPLETED = "transaction-completed"
    }
}
