package com.aleshqo.banking.transaction.kafka.producer

import com.aleshqo.banking.common.dto.TransactionDTO
import com.aleshqo.banking.common.event.TransactionTryEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID

@Component
class TransactionTryProducer(
    @Qualifier("transactionTryKafkaTemplate")
    private val kafkaTemplate: KafkaTemplate<String, TransactionTryEvent>
) {
    fun sendTryEvent(dto: TransactionDTO): String {
        val requestId = UUID.randomUUID().toString()
        val event = TransactionTryEvent(
            requestId = requestId,
            senderId = dto.senderId,
            receiverId = dto.receiverId,
            amount = dto.amount,
            timestamp = Instant.now()
        )
        kafkaTemplate.send("transaction-try", requestId, event).get()
        return requestId
    }
}
