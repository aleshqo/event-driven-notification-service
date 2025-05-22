package com.example.banking.transaction.client

import com.example.banking.common.dto.BalanceUpdateRequest
import org.springframework.stereotype.Component

@Component
class AccountClient(
    private val webClient: WebClient
) {

    fun updateBalances(request: BalanceUpdateRequest) {
        webClient.post()
            .uri("/transfer")
            .bodyValue(request)
            .retrieve()
            .onStatus({ it.is4xxClientError || it.is5xxServerError }) {
                it.bodyToMono<String>().map { msg ->
                    RuntimeException("Failed to update balances: $msg")
                }
            }
            .toBodilessEntity()
            .block()
    }
}