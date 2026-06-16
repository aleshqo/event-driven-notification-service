package com.aleshqo.banking.transaction.client

import com.aleshqo.banking.common.dto.BalanceCancelRequest
import com.aleshqo.banking.common.dto.BalanceConfirmRequest
import com.aleshqo.banking.common.dto.BalanceReserveRequest
import com.aleshqo.banking.transaction.exception.BalanceUpdateClientException
import com.aleshqo.banking.transaction.exception.BalanceUpdateServerException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientResponseException

@Component
class AccountClient(
    private val restClient: RestClient
) {

    fun reserveAmount(request: BalanceReserveRequest) {
        execute { post("/api/accounts/reserve", request) }
    }

    fun confirmTransfer(request: BalanceConfirmRequest) {
        execute { post("/api/accounts/confirm", request) }
    }

    fun cancelReservation(request: BalanceCancelRequest) {
        execute { post("/api/accounts/cancel", request) }
    }

    private fun execute(action: RestClient.() -> Unit) {
        try {
            restClient.action()
        } catch (ex: RestClientResponseException) {
            val message = ex.responseBodyAsString.ifBlank { ex.message ?: "Account service error" }
            if (ex.statusCode.is4xxClientError) {
                throw BalanceUpdateClientException(message)
            }
            throw BalanceUpdateServerException(message)
        } catch (ex: Exception) {
            throw BalanceUpdateServerException(ex.message ?: "Account service unavailable")
        }
    }

    private fun RestClient.post(uri: String, body: Any) {
        post()
            .uri(uri)
            .body(body)
            .retrieve()
            .toBodilessEntity()
    }
}
