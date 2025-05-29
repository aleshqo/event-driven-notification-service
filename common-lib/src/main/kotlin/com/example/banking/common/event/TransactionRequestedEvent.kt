package com.example.banking.common.event

import java.time.Instant
import java.util.*

data class TransactionRequestedEvent(
    val senderId: Long,
    val receiverId: Long,
    val amount: Double,
    val requestId: String = UUID.randomUUID().toString(),
    val timestamp: Instant,
)
