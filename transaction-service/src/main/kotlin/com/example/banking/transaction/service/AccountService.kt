package com.example.banking.transaction.service

import com.example.banking.transaction.entity.Account
import com.example.banking.transaction.exception.ResourceNotFoundException
import com.example.banking.transaction.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) {

    fun getById(id: Long): Account =
        accountRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Account not found: $id")
        }

    fun saveAll(accounts: List<Account>) {
        accountRepository.saveAll(accounts)
    }
}