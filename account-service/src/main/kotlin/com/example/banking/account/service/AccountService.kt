package com.example.banking.account.service

import com.example.banking.account.entity.Account
import com.example.banking.account.entity.AccountTransaction
import com.example.banking.account.repository.AccountRepository
import com.example.banking.account.repository.AccountTransactionRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val transactionRepository: AccountTransactionRepository
) {
    fun getAccount(id: Long): Account =
        accountRepository.findById(id).orElseThrow { IllegalArgumentException("Account not found") }

    fun getTransactionHistory(accountId: Long): List<AccountTransaction> =
        transactionRepository.findByAccountIdOrderByTimestampDesc(accountId)

    fun getTopAccounts(limit: Int = 5): List<Account> =
        accountRepository.findAll()
            .sortedByDescending { it.balance }
            .take(limit)
}