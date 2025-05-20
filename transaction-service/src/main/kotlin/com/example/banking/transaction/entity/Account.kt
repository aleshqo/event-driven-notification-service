package com.example.banking.transaction.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val number: String,  // Номер счета (например, "ACC123456")

    @Column(nullable = false)
    var balance: Double,

    @Column(nullable = false)
    val ownerName: String
)