package com.example.banking.common.event

import java.time.Instant

data class TransactionEvent(
    val transactionId: Long,
    val message: String,
    val timestamp: Instant = Instant.now()
)