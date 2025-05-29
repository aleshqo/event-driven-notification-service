package com.example.banking.account.service

import com.example.banking.account.entity.AccountTransaction
import com.example.banking.account.repository.AccountTransactionRepository
import com.example.banking.common.enums.Direction
import com.example.banking.common.enums.TransactionStatus
import com.example.banking.common.event.TransactionCompletedEvent
import org.springframework.stereotype.Service

@Service
class AccountTransactionService(
    private val accountTransactionRepository: AccountTransactionRepository
) {
    fun handleTransaction(event: TransactionCompletedEvent) {
        if (event.status != TransactionStatus.SUCCESS) return

        val senderTx = AccountTransaction(
            transactionId = event.transactionId,
            accountId = event.senderId,
            counterpartyId = event.receiverId,
            amount = event.amount,
            direction = Direction.OUTGOING,
            timestamp = event.timestamp,
            message = event.message
        )

        val receiverTx = senderTx.copy(
            accountId = event.receiverId,
            counterpartyId = event.senderId,
            direction = Direction.INCOMING
        )

        accountTransactionRepository.saveAll(listOf(senderTx, receiverTx))
    }
}