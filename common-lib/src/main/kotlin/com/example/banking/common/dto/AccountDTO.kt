package com.example.banking.common.dto

data class AccountDTO(
    val id: Long,
    val balance: Double,
    val ownerName: String
)