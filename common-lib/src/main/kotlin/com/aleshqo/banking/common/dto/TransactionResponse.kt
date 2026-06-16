package com.aleshqo.banking.common.dto

import com.aleshqo.banking.common.enums.TransactionStatus
import java.math.BigDecimal
import java.time.Instant

data class TransactionResponse(
    val requestId: String,
    val senderId: Long,
    val receiverId: Long,
    val amount: BigDecimal,
    val status: TransactionStatus,
    val timestamp: Instant
)
