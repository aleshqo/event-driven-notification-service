package com.example.banking.common.dto

import java.math.BigDecimal
import java.time.Instant

data class BalanceConfirmRequest(
    val senderId: Long,
    val receiverId: Long,
    val amount: BigDecimal,
    val requestId: String,
    val instant: Instant = Instant.now()
)