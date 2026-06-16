package com.aleshqo.banking.transaction.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig(
    @Value("\${account-service.base-url:http://localhost:8082}")
    private val accountServiceBaseUrl: String
) {

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .baseUrl(accountServiceBaseUrl)
            .build()
    }
}