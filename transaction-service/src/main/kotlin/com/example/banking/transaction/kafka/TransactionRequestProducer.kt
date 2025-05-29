package com.example.banking.transaction.kafka

import com.example.banking.common.dto.TransactionDTO
import com.example.banking.common.event.TransactionRequestedEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class TransactionRequestProducer(

    @Qualifier("transactionRequestedKafkaTemplate")
    private val kafkaTemplate: KafkaTemplate<String, TransactionRequestedEvent>
) {
    fun sendTransferRequest(dto: TransactionDTO) {
        val event = TransactionRequestedEvent(
            requestId = UUID.randomUUID().toString(),
            senderId = dto.senderId,
            receiverId = dto.receiverId,
            amount = dto.amount,
            timestamp = Instant.now(),
        )
        kafkaTemplate.send("transfer-requests", event)
    }
}
