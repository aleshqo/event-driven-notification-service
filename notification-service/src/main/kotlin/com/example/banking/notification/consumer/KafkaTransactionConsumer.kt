package com.example.banking.notification.consumer

import com.example.banking.common.event.TransactionEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaTransactionConsumer {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["transactions"])
    fun listen(event: TransactionEvent) {
        logger.info("Notification sent: $event")
        // добавить реальную отправку email
    }
}