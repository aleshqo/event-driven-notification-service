package com.example.banking.transaction.entity

import com.example.banking.common.enums.TransactionStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var requestId: String,

    @Column(nullable = false)
    var senderId: Long,

    @Column(nullable = false)
    var receiverId: Long,

    @Column(nullable = false)
    var amount: BigDecimal,

    @Column(nullable = false)
    var timestamp: Instant = Instant.now(),

    var status: TransactionStatus = TransactionStatus.PENDING
)