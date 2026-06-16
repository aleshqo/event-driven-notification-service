package com.aleshqo.banking.transaction.service

import com.aleshqo.banking.common.dto.BalanceCancelRequest
import com.aleshqo.banking.common.dto.BalanceConfirmRequest
import com.aleshqo.banking.common.dto.BalanceReserveRequest
import com.aleshqo.banking.common.dto.TransactionResponse
import com.aleshqo.banking.common.enums.TransactionStatus
import com.aleshqo.banking.common.event.TransactionCancelEvent
import com.aleshqo.banking.common.event.TransactionConfirmEvent
import com.aleshqo.banking.common.event.TransactionTryEvent
import com.aleshqo.banking.transaction.client.AccountClient
import com.aleshqo.banking.transaction.exception.TransactionNotFoundException
import com.aleshqo.banking.transaction.repository.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TransactionProcessingService(
    private val accountClient: AccountClient,
    private val persistence: TransactionPersistence,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun tryTransaction(event: TransactionTryEvent) {
        log.info("Try transaction requestId={}", event.requestId)

        if (persistence.exists(event.requestId)) {
            log.info("Skip duplicate try event requestId={}", event.requestId)
            return
        }

        try {
            persistence.createPending(event)
            accountClient.reserveAmount(
                BalanceReserveRequest(
                    accountId = event.senderId,
                    amount = event.amount,
                    requestId = event.requestId
                )
            )
            persistence.enqueueConfirm(event)
        } catch (e: Exception) {
            log.error("Try transaction failed requestId={}", event.requestId, e)
            persistence.markCanceled(event.requestId)
            persistence.enqueueCancel(event, e.message ?: "unknown")
        }
    }

    fun confirmTransaction(event: TransactionConfirmEvent) {
        log.info("Confirm transaction requestId={}", event.requestId)

        val transaction = persistence.findByRequestId(event.requestId)
            ?: throw TransactionNotFoundException(event.requestId)

        if (transaction.status != TransactionStatus.PENDING) {
            log.info(
                "Skip confirm for non-pending transaction requestId={} status={}",
                event.requestId,
                transaction.status
            )
            return
        }

        try {
            accountClient.confirmTransfer(
                BalanceConfirmRequest(
                    senderId = event.senderId,
                    receiverId = event.receiverId,
                    amount = event.amount,
                    requestId = event.requestId
                )
            )
            persistence.markConfirmed(transaction, event)
        } catch (e: Exception) {
            log.error("Confirm transaction failed requestId={}", event.requestId, e)
            persistence.markFailed(event.requestId)
            persistence.enqueueCancelFromConfirm(event, e.message ?: "unknown")
        }
    }

    fun cancelTransaction(event: TransactionCancelEvent) {
        log.info("Cancel transaction requestId={}", event.requestId)

        val transaction = persistence.findByRequestId(event.requestId)
            ?: run {
                log.info("Skip cancel for unknown transaction requestId={}", event.requestId)
                return
            }

        if (transaction.status != TransactionStatus.PENDING &&
            transaction.status != TransactionStatus.FAILED
        ) {
            log.info(
                "Skip cancel for transaction requestId={} status={}",
                event.requestId,
                transaction.status
            )
            return
        }

        try {
            accountClient.cancelReservation(
                BalanceCancelRequest(
                    accountId = event.senderId,
                    amount = event.amount,
                    requestId = event.requestId
                )
            )
            persistence.markCanceled(event.requestId)
        } catch (e: Exception) {
            log.error("Cancel transaction failed requestId={}", event.requestId, e)
        }
    }
}

@Service
class TransactionQueryService(
    private val transactionRepository: TransactionRepository
) {

    fun getByRequestId(requestId: String): TransactionResponse {
        val transaction = transactionRepository.findByRequestId(requestId)
            ?: throw TransactionNotFoundException(requestId)

        return TransactionResponse(
            requestId = transaction.requestId,
            senderId = transaction.senderId,
            receiverId = transaction.receiverId,
            amount = transaction.amount,
            status = transaction.status,
            timestamp = transaction.timestamp
        )
    }
}
