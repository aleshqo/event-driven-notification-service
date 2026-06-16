package com.aleshqo.banking.account.service

import com.aleshqo.banking.account.entity.Account
import com.aleshqo.banking.account.entity.AccountTransaction
import com.aleshqo.banking.account.exception.AccountNotFoundException
import com.aleshqo.banking.account.repository.AccountRepository
import com.aleshqo.banking.account.repository.AccountTransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val transactionRepository: AccountTransactionRepository
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun getAccount(id: Long): Account {
        log.info("Get account id={}", id)
        return accountRepository.findById(id)
            .orElseThrow { AccountNotFoundException(id) }
    }

    fun getTransactionHistory(accountId: Long): List<AccountTransaction> {
        log.info("Get transactions accountId={}", accountId)
        return transactionRepository.findByAccountIdOrderByTimestampDesc(accountId)
    }

    fun getTopAccounts(limit: Int): List<Account> {
        log.info("Get top accounts limit={}", limit)
        return accountRepository.findAllByOrderByAvailableBalanceDesc(PageRequest.of(0, limit))
    }
}
