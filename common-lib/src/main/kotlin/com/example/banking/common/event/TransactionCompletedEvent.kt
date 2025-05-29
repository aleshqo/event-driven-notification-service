package com.example.banking.common.event

import com.example.banking.common.enums.TransactionStatus
import java.time.Instant

data class TransactionCompletedEvent(
    val transactionId: Long,
    val senderId: Long,
    val receiverId: Long,
    val amount: Double,
    val timestamp: Instant,
    val status: TransactionStatus,
    val message: String
)
