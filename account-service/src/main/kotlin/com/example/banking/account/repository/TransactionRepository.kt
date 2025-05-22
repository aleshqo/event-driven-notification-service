package com.example.banking.account.repository

import com.example.banking.account.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, Long> {
    fun findAllBySenderIdOrReceiverId(senderId: Long, receiverId: Long): List<Transaction>
}