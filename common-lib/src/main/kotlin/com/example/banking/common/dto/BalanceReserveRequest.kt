package com.example.banking.common.dto

import java.math.BigDecimal

data class BalanceReserveRequest(
    val accountId: Long,
    val amount: BigDecimal,
    val requestId: String
)