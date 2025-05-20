package com.example.banking.common.dto

import jakarta.validation.constraints.Positive

data class TransactionDTO(
    val senderId: Long,
    val receiverId: Long,
    @field:Positive(message = "Amount must be positive")
    val amount: Double
) {
    init {
        require(senderId != receiverId) { "Sender and receiver must be different" }
        require(amount > 0) { "Amount must be positive" }
    }
}