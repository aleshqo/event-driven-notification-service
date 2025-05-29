package com.example.banking.transaction.kafka.consumer

import com.example.banking.common.event.TransactionCancelEvent
import com.example.banking.transaction.service.TransactionProcessingService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionCancelConsumer(
    private val transactionProcessingService: TransactionProcessingService
) {
    @KafkaListener(topics = ["transaction-cancel"], groupId = "transaction-group")
    fun onCancel(event: TransactionCancelEvent) {
        transactionProcessingService.cancelTransaction(event)
    }
}
