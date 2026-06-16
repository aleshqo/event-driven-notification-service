package com.aleshqo.banking.transaction.outbox

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OutboxService(
    private val outboxEventRepository: OutboxEventRepository,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun enqueue(topic: String, messageKey: String, payload: Any) {
        outboxEventRepository.save(
            OutboxEvent(
                topic = topic,
                messageKey = messageKey,
                payload = objectMapper.writeValueAsString(payload)
            )
        )
    }
}
