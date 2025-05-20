package com.example.banking.transaction.repository

import com.example.banking.transaction.entity.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByNumber(number: String): Account?
}