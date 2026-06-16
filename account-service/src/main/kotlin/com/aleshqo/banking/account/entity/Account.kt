package com.aleshqo.banking.account.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "accounts")
class Account(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var number: String,

    @Column(nullable = false)
    var ownerName: String,

    @Column(nullable = false, precision = 19, scale = 2)
    var availableBalance: BigDecimal,

    @Column(nullable = false, precision = 19, scale = 2)
    var reservedBalance: BigDecimal,

    @Version
    var version: Long? = null
)