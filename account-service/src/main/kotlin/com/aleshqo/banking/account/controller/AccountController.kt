package com.aleshqo.banking.account.controller

import com.aleshqo.banking.account.entity.Account
import com.aleshqo.banking.account.entity.AccountTransaction
import com.aleshqo.banking.account.service.AccountService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/accounts")
class AccountController(
    private val accountService: AccountService,

    ) {
    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: Long): Account =
        accountService.getAccount(id)

    @GetMapping("/{accountId}/transactions")
    fun getTransactionHistory(@PathVariable accountId: Long): List<AccountTransaction> =
        accountService.getTransactionHistory(accountId)

    @GetMapping("/top")
    fun getTopAccounts(@RequestParam(defaultValue = "5") limit: Int): List<Account> =
        accountService.getTopAccounts(limit)
}