package com.example.banking.account.service

import com.example.banking.account.entity.Account
import com.example.banking.account.entity.Transaction
import com.example.banking.account.repository.AccountRepository
import com.example.banking.account.repository.TransactionRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository
) {
    fun getAccount(id: Long): Account =
        accountRepository.findById(id).orElseThrow { IllegalArgumentException("Account not found") }

    fun getTransactionHistory(accountId: Long): List<Transaction> =
        transactionRepository.findAllBySenderIdOrReceiverId(accountId, accountId)

    fun getTopAccounts(limit: Int = 5): List<Account> =
        accountRepository.findAll()
            .sortedByDescending { it.balance }
            .take(limit)
}