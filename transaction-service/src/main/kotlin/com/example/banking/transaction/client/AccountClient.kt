package com.example.banking.transaction.client

import com.example.banking.common.dto.BalanceUpdateRequest
import com.example.banking.transaction.exception.BalanceUpdateClientException
import com.example.banking.transaction.exception.BalanceUpdateServerException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AccountClient{

    private val webClient: WebClient = WebClient.create()

    fun updateBalances(request: BalanceUpdateRequest): Mono<Void> {
        return webClient.post()
            .uri("/transfer")
            .bodyValue(request)
            .retrieve()
            .onStatus({ it.is4xxClientError }) { response ->
                response.bodyToMono(String::class.java)
                    .map { msg -> BalanceUpdateClientException(msg) }
            }
            .onStatus({ it.is5xxServerError }) { response ->
                response.bodyToMono(String::class.java)
                    .map { msg -> BalanceUpdateServerException(msg) }
            }
            .toBodilessEntity()
            .then()
    }
}