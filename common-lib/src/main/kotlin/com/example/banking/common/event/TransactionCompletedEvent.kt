package com.example.banking.common.event

data class TransactionCompletedEvent(
    val transactionId: Long,
    val status: TransactionStatus,
    val message: String
)
