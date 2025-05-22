package com.example.banking.transaction.service

import com.example.banking.common.dto.BalanceUpdateRequest
import com.example.banking.common.event.TransactionCompletedEvent
import com.example.banking.common.event.TransactionRequestedEvent
import com.example.banking.common.event.TransactionStatus
import com.example.banking.transaction.client.AccountClient
import com.example.banking.transaction.entity.ProcessedRequest
import com.example.banking.transaction.entity.Transaction
import com.example.banking.transaction.repository.ProcessedRequestRepository
import com.example.banking.transaction.repository.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionProcessingService(
    private val accountClient: AccountClient,
    private val transactionRepository: TransactionRepository,
    private val processedRequestRepository: ProcessedRequestRepository,
    private val kafkaTemplate: KafkaTemplate<String, TransactionCompletedEvent>
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @Transactional
    fun processTransfer(event: TransactionRequestedEvent) {
        try {
            // Проверка идемпотентности
            if (processedRequestRepository.existsById(event.requestId)) {
                logger.warn("Request ${event.requestId} already processed, skipping.")
                return
            }

            accountClient.updateBalances(
                BalanceUpdateRequest(
                    senderId = event.senderId,
                    receiverId = event.receiverId,
                    amount = event.amount,
                    requestId = event.requestId
                )
            )

            val tx = transactionRepository.save(
                Transaction(
                    senderId = event.senderId,
                    receiverId = event.receiverId,
                    amount = event.amount
                )
            )

            // Помечаем как обработанный
            processedRequestRepository.save(ProcessedRequest(requestId = event.requestId))

            kafkaTemplate.send(
                "transfer-completed",
                TransactionCompletedEvent(
                    transactionId = tx.id!!,
                    status = TransactionStatus.SUCCESS,
                    message = "Transaction complete"
                )
            )

        } catch (ex: Exception) {
            kafkaTemplate.send(
                "transfer-completed",
                TransactionCompletedEvent(
                    transactionId = -1,
                    status = TransactionStatus.FAILED,
                    message = "Error: ${ex.message}"
                )
            )
        }
    }
}

