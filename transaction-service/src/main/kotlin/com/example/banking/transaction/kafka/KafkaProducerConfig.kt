package com.example.banking.transaction.kafka

import com.example.banking.common.event.TransactionCompletedEvent
import com.example.banking.common.event.TransactionRequestedEvent
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String
) {

    private fun commonConfigs() = mapOf(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
    )

    @Bean
    fun transactionRequestedProducerFactory(): ProducerFactory<String, TransactionRequestedEvent> =
        DefaultKafkaProducerFactory(commonConfigs())

    @Bean
    fun transactionCompletedProducerFactory(): ProducerFactory<String, TransactionCompletedEvent> =
        DefaultKafkaProducerFactory(commonConfigs())

    @Bean
    fun transactionRequestedKafkaTemplate(): KafkaTemplate<String, TransactionRequestedEvent> =
        KafkaTemplate(transactionRequestedProducerFactory())

    @Bean
    fun transactionCompletedKafkaTemplate(): KafkaTemplate<String, TransactionCompletedEvent> =
        KafkaTemplate(transactionCompletedProducerFactory())
}