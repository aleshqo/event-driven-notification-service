package com.example.banking.common.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Schema(description = "Transaction request")
data class TransactionDTO(
    @field:NotNull
    val senderId: Long,
    @field:NotNull
    val receiverId: Long,
    @field:Positive(message = "Amount must be positive")
    @field:DecimalMin("0.01")
    val amount: Double
) {
    init {
        require(senderId != receiverId) { "Sender and receiver must be different" }
        require(amount > 0) { "Amount must be positive" }
    }
}