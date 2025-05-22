package com.example.banking.common.dto

data class BalanceUpdateRequest(
    val senderId: Long,
    val receiverId: Long,
    val amount: Double,
    val requestId: String
)