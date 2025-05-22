package com.example.banking.account.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val senderId: Long,
    val receiverId: Long,
    val amount: Double,
    val timestamp: Instant
)