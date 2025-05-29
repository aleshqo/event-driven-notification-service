package com.example.banking.transaction.client

import com.example.banking.common.dto.BalanceCancelRequest
import com.example.banking.common.dto.BalanceConfirmRequest
import com.example.banking.common.dto.BalanceReserveRequest
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AccountClient{

    private val webClient: WebClient = WebClient.create()

    fun reserveAmount(request: BalanceReserveRequest): Mono<Void> {
        return webClient.post()
            .uri("/api/accounts/reserve")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Void::class.java)
    }

    fun confirmTransfer(request: BalanceConfirmRequest): Mono<Void> {
        return webClient.post()
            .uri("/api/accounts/confirm")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Void::class.java)
    }

    fun cancelReservation(request: BalanceCancelRequest): Mono<Void> {
        return webClient.post()
            .uri("/api/accounts/cancel")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Void::class.java)
    }
}