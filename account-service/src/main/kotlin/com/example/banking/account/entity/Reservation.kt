package com.example.banking.account.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "reservations")
data class Reservation(
    @Id
    var requestId: String, // requestId — используется как уникальный идентификатор TCC операции

    var accountId: Long,

    @Column(precision = 19, scale = 4)
    var amount: BigDecimal,

    var createdAt: Instant = Instant.now()
)