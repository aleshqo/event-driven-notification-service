package com.aleshqo.banking.transaction.entity

import com.aleshqo.banking.common.enums.TransactionStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true, length = 64)
    var requestId: String,

    @Column(nullable = false)
    var senderId: Long,

    @Column(nullable = false)
    var receiverId: Long,

    @Column(nullable = false)
    var amount: BigDecimal,

    @Column(nullable = false)
    var timestamp: Instant = Instant.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    var status: TransactionStatus = TransactionStatus.PENDING
)