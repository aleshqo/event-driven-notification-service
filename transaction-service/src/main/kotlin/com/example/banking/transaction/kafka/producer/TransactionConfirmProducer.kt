package com.example.banking.transaction.kafka.producer

import com.example.banking.common.event.TransactionConfirmEvent
import com.example.banking.common.event.TransactionTryEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class TransactionConfirmProducer(
    @Qualifier("transactionConfirmKafkaTemplate")
    private val kafkaTemplate: KafkaTemplate<String, TransactionConfirmEvent>
) {
    fun sendConfirmEvent(event: TransactionTryEvent) {
        val confirmEvent = TransactionConfirmEvent(
            requestId = event.requestId,
            senderId = event.senderId,
            receiverId = event.receiverId,
            amount = event.amount,
            timestamp = Instant.now()
        )
        kafkaTemplate.send("transaction-confirm", confirmEvent)
    }
}