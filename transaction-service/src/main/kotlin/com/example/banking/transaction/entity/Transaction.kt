package com.example.banking.transaction.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    val sender: Account,

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    val receiver: Account,

    @Column(nullable = false)
    val amount: Double,

    @Column(nullable = false)
    val timestamp: Instant = Instant.now()
)