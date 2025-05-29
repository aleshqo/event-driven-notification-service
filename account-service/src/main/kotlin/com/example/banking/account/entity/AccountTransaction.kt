package com.example.banking.account.entity

import com.example.banking.common.enums.Direction
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "account_transactions")
data class AccountTransaction(
    @Id
    var transactionId: Long,

    var accountId: Long,
    var counterpartyId: Long,
    var amount: Double,
    var direction: Direction,
    var timestamp: Instant,
    var message: String
)
