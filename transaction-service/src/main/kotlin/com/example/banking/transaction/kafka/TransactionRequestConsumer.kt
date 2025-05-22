package com.example.banking.transaction.kafka

import com.example.banking.common.event.TransactionRequestedEvent
import com.example.banking.transaction.service.TransactionProcessingService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionRequestConsumer(
    private val transactionProcessingService: TransactionProcessingService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)


    @KafkaListener(topics = ["transfer-requests"], groupId = "transaction-group")
    fun handle(event: TransactionRequestedEvent) {
        logger.info("Received transaction request: ${event.requestId}")
        transactionProcessingService.processTransfer(event)
    }
}