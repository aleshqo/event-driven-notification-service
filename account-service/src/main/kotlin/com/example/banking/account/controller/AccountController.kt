package com.example.banking.account.controller

import com.example.banking.account.entity.Account
import com.example.banking.account.entity.Transaction
import com.example.banking.account.service.AccountService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService
) {

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: Long): Account = accountService.getAccount(id)

    @GetMapping("/{id}/transactions")
    fun getTransactions(@PathVariable id: Long): List<Transaction> =
        accountService.getTransactionHistory(id)

    @GetMapping("/top")
    fun getTopAccounts(@RequestParam(defaultValue = "5") limit: Int): List<Account> =
        accountService.getTopAccounts(limit)
}