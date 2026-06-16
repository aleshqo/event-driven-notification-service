package com.aleshqo.banking.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "reservations")
class Reservation(

    @Id
    var requestId: String,

    var accountId: Long,

    @Column(nullable = false, precision = 19, scale = 2)
    var amount: BigDecimal,

    var createdAt: Instant = Instant.now()
)