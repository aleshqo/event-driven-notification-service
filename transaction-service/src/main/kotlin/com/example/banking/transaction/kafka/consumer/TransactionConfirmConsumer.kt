package com.example.banking.transaction.kafka.consumer

import com.example.banking.common.event.TransactionConfirmEvent
import com.example.banking.transaction.service.TransactionProcessingService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionConfirmConsumer(
    private val transactionProcessingService: TransactionProcessingService
) {
    @KafkaListener(topics = ["transaction-confirm"], groupId = "transaction-group")
    fun onConfirm(event: TransactionConfirmEvent) {
        transactionProcessingService.confirmTransaction(event)
    }
}