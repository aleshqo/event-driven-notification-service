package com.example.banking.account.entity

import jakarta.persistence.*

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var number: String,

    @Column(nullable = false)
    var ownerName: String,

    @Column(nullable = false)
    var balance: Double
)