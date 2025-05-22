package com.example.banking.account.entity

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val number: String,

    @Column(nullable = false)
    val ownerName: String,

    @Column(nullable = false)
    var balance: Double
)