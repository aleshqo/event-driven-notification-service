package com.example.banking.account.service

import com.example.banking.account.dto.BalanceUpdateRequest
import com.example.banking.account.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class BalanceService(
    private val accountRepository: AccountRepository,
) {

    @Transactional
    open fun transfer(request: BalanceUpdateRequest) {
        val sender = accountRepository.findByIdForUpdate(request.senderId)
            ?: throw IllegalArgumentException("Sender not found")

        val receiver = accountRepository.findByIdForUpdate(request.receiverId)
            ?: throw IllegalArgumentException("Receiver not found")

        if (sender.balance < request.amount) {
            throw IllegalArgumentException("Insufficient funds")
        }

        sender.balance -= request.amount
        receiver.balance += request.amount
        accountRepository.saveAll(listOf(sender, receiver))
    }
}
