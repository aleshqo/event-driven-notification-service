package com.example.banking.transaction.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var senderId: Long,

    @Column(nullable = false)
    var receiverId: Long,

    @Column(nullable = false)
    var amount: Double,

    @Column(nullable = false)
    var timestamp: Instant = Instant.now()
)