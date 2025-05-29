package com.example.banking.account.service

import com.example.banking.account.dto.BalanceUpdateRequest
import com.example.banking.account.exception.InsufficientFundsException
import com.example.banking.account.exception.ResourceNotFoundException
import com.example.banking.account.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BalanceService(
    private val accountRepository: AccountRepository,
) {

    @Transactional
    fun transfer(request: BalanceUpdateRequest) {
        val sender = accountRepository.findByIdForUpdate(request.senderId)
            ?: throw ResourceNotFoundException("Sender not found")

        val receiver = accountRepository.findByIdForUpdate(request.receiverId)
            ?: throw ResourceNotFoundException("Receiver not found")

        if (sender.balance < request.amount) {
            throw InsufficientFundsException("Insufficient funds")
        }

        sender.balance -= request.amount
        receiver.balance += request.amount
        accountRepository.saveAll(listOf(sender, receiver))
    }
}
