package com.aleshqo.banking.account.entity

import com.aleshqo.banking.common.enums.Direction
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "account_transactions")
class AccountTransaction(
    @Id
    var transactionId: Long,

    var accountId: Long,
    var counterpartyId: Long,

    @Column(nullable = false, precision = 19, scale = 2)
    var amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    var direction: Direction,

    var timestamp: Instant = Instant.now(),
    var message: String? = null
)
