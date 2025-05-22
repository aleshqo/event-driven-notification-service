package com.example.banking.transaction.entity

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    val id: Long,

    @Column(nullable = false)
    var number: String,

    @Column(nullable = false)
    var balance: Double,

    @Column(nullable = false)
    var ownerName: String,

    @Version
    val version: Long? = null
)