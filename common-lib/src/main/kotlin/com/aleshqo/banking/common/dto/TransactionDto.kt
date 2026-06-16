package com.aleshqo.banking.common.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

//@Schema(description = "Transaction request")
data class TransactionDTO(
    @field:NotNull
    val senderId: Long,
    @field:NotNull
    val receiverId: Long,
    @field:Positive(message = "Amount must be positive")
    @field:DecimalMin("0.01")
    val amount: BigDecimal
) {
    init {
        require(senderId != receiverId) { "Sender and receiver must be different" }
        require(amount > BigDecimal(0)) { "Amount must be positive" }
    }
}