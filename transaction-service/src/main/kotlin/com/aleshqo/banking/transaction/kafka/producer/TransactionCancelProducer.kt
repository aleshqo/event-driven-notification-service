package com.aleshqo.banking.transaction.kafka.producer

import com.aleshqo.banking.common.event.TransactionCancelEvent
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Instant

@Component
class TransactionCancelProducer(
    @Qualifier("transactionCancelKafkaTemplate")
    private val kafkaTemplate: KafkaTemplate<String, TransactionCancelEvent>
) {
    fun sendCancelEvent(
        requestId: String,
        senderId: Long,
        amount: BigDecimal,
        reason: String
    ) {
        val cancelEvent = TransactionCancelEvent(
            requestId = requestId,
            senderId = senderId,
            amount = amount,
            timestamp = Instant.now(),
            reason = reason
        )
        kafkaTemplate.send("transaction-cancel", cancelEvent)
    }
}
