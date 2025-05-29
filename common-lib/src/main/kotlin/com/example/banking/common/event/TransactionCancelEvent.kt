package com.example.banking.common.event

import java.math.BigDecimal
import java.time.Instant

data class TransactionCancelEvent(
    val requestId: String,
    val senderId: Long,
    val amount: BigDecimal,
    val timestamp: Instant,
    val reason: String
)