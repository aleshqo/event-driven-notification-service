package com.aleshqo.banking.common.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class BalanceReserveRequest(
    @field:NotNull
    val accountId: Long,

    @field:Positive
    @field:DecimalMin("0.01")
    val amount: BigDecimal,

    @field:NotBlank
    val requestId: String
)