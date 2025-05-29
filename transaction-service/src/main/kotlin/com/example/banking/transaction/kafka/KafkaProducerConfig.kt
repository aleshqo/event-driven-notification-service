package com.example.banking.transaction.kafka

import com.example.banking.common.event.TransactionCancelEvent
import com.example.banking.common.event.TransactionConfirmEvent
import com.example.banking.common.event.TransactionTryEvent
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapServers: String

    private fun producerFactory(): Map<String, Any> = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
        JsonSerializer.ADD_TYPE_INFO_HEADERS to false // Убираем тип в headers (можно true, если надо)
    )

    @Bean
    fun transactionTryKafkaTemplate(): KafkaTemplate<String, TransactionTryEvent> {
        return KafkaTemplate(DefaultKafkaProducerFactory(producerFactory()))
    }

    @Bean
    fun transactionConfirmKafkaTemplate(): KafkaTemplate<String, TransactionConfirmEvent> {
        return KafkaTemplate(DefaultKafkaProducerFactory(producerFactory()))
    }

    @Bean
    fun transactionCancelKafkaTemplate(): KafkaTemplate<String, TransactionCancelEvent> {
        return KafkaTemplate(DefaultKafkaProducerFactory(producerFactory()))
    }
}
