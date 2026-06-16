package com.aleshqo.banking.transaction.kafka.consumer

import com.aleshqo.banking.common.event.TransactionCancelEvent
import com.aleshqo.banking.transaction.service.TransactionProcessingService
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
