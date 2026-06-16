package com.aleshqo.banking.common.event

import java.math.BigDecimal
import java.time.Instant

data class TransactionConfirmEvent(
    val requestId: String,
    val senderId: Long,
    val receiverId: Long,
    val amount: BigDecimal,
    val timestamp: Instant
)
