package com.aleshqo.banking.account.repository

import com.aleshqo.banking.account.entity.AccountTransaction
import org.springframework.data.jpa.repository.JpaRepository

interface AccountTransactionRepository : JpaRepository<AccountTransaction, Long> {
    fun findByAccountIdOrderByTimestampDesc(accountId: Long): List<AccountTransaction>
}