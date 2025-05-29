package com.example.banking.notification.consumer

import com.example.banking.common.event.TransactionCompletedEvent
import com.example.banking.notification.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionCompletedConsumer(
    private val notificationService: NotificationService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["transactions"])
    fun listen(event: TransactionCompletedEvent) {
        notificationService.sendNotification(event)
        logger.info("Notification sent: $event")
        // добавить реальную отправку email
    }
}