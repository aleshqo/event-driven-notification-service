package com.aleshqo.banking.transaction.kafka.consumer

import com.aleshqo.banking.common.event.TransactionConfirmEvent
import com.aleshqo.banking.transaction.service.TransactionProcessingService
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