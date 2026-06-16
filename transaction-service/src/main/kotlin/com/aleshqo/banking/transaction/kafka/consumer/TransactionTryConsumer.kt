package com.aleshqo.banking.transaction.kafka.consumer

import com.aleshqo.banking.common.event.TransactionTryEvent
import com.aleshqo.banking.transaction.service.TransactionProcessingService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionTryConsumer(
    private val transactionProcessingService: TransactionProcessingService
) {
    @KafkaListener(topics = ["transaction-try"], groupId = "transaction-group")
    fun onTryEvent(event: TransactionTryEvent) {
        transactionProcessingService.tryTransaction(event)
    }
}