package com.example.banking.transaction.service

import com.example.banking.common.dto.BalanceCancelRequest
import com.example.banking.common.dto.BalanceConfirmRequest
import com.example.banking.common.dto.BalanceReserveRequest
import com.example.banking.common.enums.TransactionStatus
import com.example.banking.common.event.TransactionCancelEvent
import com.example.banking.common.event.TransactionConfirmEvent
import com.example.banking.common.event.TransactionTryEvent
import com.example.banking.transaction.client.AccountClient
import com.example.banking.transaction.entity.Transaction
import com.example.banking.transaction.kafka.producer.TransactionCancelProducer
import com.example.banking.transaction.kafka.producer.TransactionConfirmProducer
import com.example.banking.transaction.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class TransactionProcessingService(
    private val accountClient: AccountClient,
    private val transactionRepository: TransactionRepository,
    private val confirmProducer: TransactionConfirmProducer,
    private val cancelProducer: TransactionCancelProducer,
) {

    fun tryTransaction(event: TransactionTryEvent) {
        val transaction = Transaction(
            requestId = event.requestId,
            senderId = event.senderId,
            receiverId = event.receiverId,
            amount = event.amount,
            status = TransactionStatus.PENDING
        )
        // Резервируем средства на аккаунте отправителя
        accountClient.reserveAmount(
            BalanceReserveRequest(
                accountId = event.senderId,
                amount = event.amount,
                requestId = event.requestId
            )
        ).subscribe(
            {
                transactionRepository.save(transaction)
                confirmProducer.sendConfirmEvent(event)
            },
            { error ->
                println("Ошибка при резервировании средств: ${error.message}")
                cancelProducer.sendCancelEvent(
                    requestId = event.requestId,
                    senderId = event.senderId,
                    amount = event.amount,
                    reason = "Ошибка TRY: ${error.message}"
                )
            }
        )
    }

    fun confirmTransaction(event: TransactionConfirmEvent) {
        transactionRepository.findByRequestId(event.requestId)?.let { transaction ->
            if (transaction.status != TransactionStatus.PENDING) {
                println("Транзакция уже обработана или отменена.")
                return
            }

            accountClient.confirmTransfer(
                BalanceConfirmRequest(
                    senderId = event.senderId,
                    receiverId = event.receiverId,
                    amount = event.amount,
                    requestId = event.requestId
                )
            ).subscribe(
                {
                    transaction.status = TransactionStatus.CONFIRMED
                    transactionRepository.save(transaction)
                    println("Транзакция подтверждена: ${transaction.id}")
                },
                { error ->
                    println("Ошибка подтверждения: ${error.message}")
                    cancelProducer.sendCancelEvent(
                        requestId = event.requestId,
                        senderId = event.senderId,
                        amount = event.amount,
                        reason = "Ошибка CONFIRM: ${error.message}"
                    )
                }
            )
        } ?: println("Транзакция ${event.requestId} не найдена.")
    }

    fun cancelTransaction(event: TransactionCancelEvent) {
        val transaction = transactionRepository.findByRequestId(event.requestId)

        if (transaction == null) {
            println("Не найдена транзакция для отмены: ${event.requestId}")
            return
        }

        if (transaction.status != TransactionStatus.PENDING) {
            println("Транзакция уже подтверждена или отменена.")
            return
        }

        accountClient.cancelReservation(
            BalanceCancelRequest(
                accountId = event.senderId,
                amount = event.amount,
                requestId = event.requestId
            )
        ).subscribe(
            {
                transaction.status = TransactionStatus.CANCELED
                transactionRepository.save(transaction)
                println("Транзакция отменена: ${transaction.id}")
            },
            { error ->
                println("Ошибка отмены резерва: ${error.message}")
            }
        )
    }
}
