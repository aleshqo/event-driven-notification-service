package com.example.banking.transaction.service

import com.example.banking.common.dto.BalanceUpdateRequest
import com.example.banking.common.enums.TransactionStatus
import com.example.banking.common.event.TransactionCompletedEvent
import com.example.banking.common.event.TransactionRequestedEvent
import com.example.banking.transaction.client.AccountClient
import com.example.banking.transaction.entity.ProcessedRequest
import com.example.banking.transaction.entity.Transaction
import com.example.banking.transaction.repository.ProcessedRequestRepository
import com.example.banking.transaction.repository.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionProcessingService(
    private val accountClient: AccountClient,
    private val transactionRepository: TransactionRepository,
    private val processedRequestRepository: ProcessedRequestRepository,
    @Qualifier("transactionCompletedKafkaTemplate")
    private val kafkaTemplate: KafkaTemplate<String, TransactionCompletedEvent>
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


    @Transactional
    fun processTransfer(event: TransactionRequestedEvent) {
        try {
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
            processedRequestRepository.save(ProcessedRequest(requestId = event.requestId))

            kafkaTemplate.send(
                "transfer-completed",
                TransactionCompletedEvent(
                    transactionId = tx.id!!,
                    senderId = event.senderId,
                    receiverId = event.receiverId,
                    amount = event.amount,
                    timestamp = event.timestamp,
                    status = TransactionStatus.SUCCESS,
                    message = "Transaction complete"
                )
            )

        } catch (ex: Exception) {
            kafkaTemplate.send(
                "transfer-completed",
                TransactionCompletedEvent(
                    transactionId = -1,
                    senderId = event.senderId,
                    receiverId = event.receiverId,
                    amount = event.amount,
                    timestamp = event.timestamp,
                    status = TransactionStatus.FAILED,
                    message = "Error: ${ex.message}"
                )
            )
        }
    }
}

