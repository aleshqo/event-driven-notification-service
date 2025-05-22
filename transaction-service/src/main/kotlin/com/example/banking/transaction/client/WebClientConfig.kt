package com.example.banking.transaction.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebClientConfig {
    @Bean
    fun accountWebClient(): WebClient =
        WebClient.builder()
            .baseUrl("http://account-service:8082/api/accounts")
            .build()
}