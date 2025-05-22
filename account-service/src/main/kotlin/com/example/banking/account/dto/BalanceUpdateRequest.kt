package com.example.banking.account.dto

data class BalanceUpdateRequest(
    val senderId: Long,
    val receiverId: Long,
    val amount: Double,
    val requestId: String
)