package com.aleshqo.banking.notification.kafka

import com.aleshqo.banking.common.event.TransactionCompletedEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class TransactionCompletedConsumer {

    private val log = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(topics = ["transaction-completed"], groupId = "notification-group")
    fun onCompleted(event: TransactionCompletedEvent) {
        log.info(
            "Notification sent: transfer {} from account {} to account {} amount {} status {}",
            event.requestId,
            event.senderId,
            event.receiverId,
            event.amount,
            event.status
        )
    }
}
