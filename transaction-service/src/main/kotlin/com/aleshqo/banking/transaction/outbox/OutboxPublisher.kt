package com.aleshqo.banking.transaction.outbox

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Component
class OutboxPublisher(
    private val outboxEventRepository: OutboxEventRepository,
    @Qualifier("outboxKafkaTemplate")
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "\${outbox.poll-interval-ms:1000}")
    @Transactional
    fun publishPendingEvents() {
        val events = outboxEventRepository.findUnpublished()
        if (events.isEmpty()) return

        events.forEach { event ->
            try {
                kafkaTemplate.send(event.topic, event.messageKey, event.payload).get()
                event.published = true
                event.publishedAt = Instant.now()
                outboxEventRepository.save(event)
                log.debug("Published outbox event id={} topic={}", event.id, event.topic)
            } catch (ex: Exception) {
                log.error("Failed to publish outbox event id={}", event.id, ex)
            }
        }
    }
}
