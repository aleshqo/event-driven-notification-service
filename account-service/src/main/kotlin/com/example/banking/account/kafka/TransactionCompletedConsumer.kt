package com.example.banking.account.kafka

import com.example.banking.account.service.AccountTransactionService
import com.example.banking.common.event.TransactionCompletedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionCompletedConsumer(
    private val transactionService: AccountTransactionService
) {
    @KafkaListener(
        topics = ["transfer-completed"],
        groupId = "account-service-group",
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listen(event: TransactionCompletedEvent) {
        transactionService.handleTransaction(event)
    }
}