package com.example.banking.transaction.service

import com.example.banking.common.dto.TransactionDTO
import com.example.banking.common.event.TransactionEvent
import com.example.banking.transaction.entity.Transaction
import com.example.banking.transaction.repository.AccountRepository
import com.example.banking.transaction.repository.TransactionRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val kafkaTemplate: KafkaTemplate<String, TransactionEvent>
) {
    @Transactional
    fun transfer(dto: TransactionDTO): Transaction {
        val sender = accountRepository.findById(dto.senderId)
            .orElseThrow { IllegalArgumentException("Sender account not found") }
        val receiver = accountRepository.findById(dto.receiverId)
            .orElseThrow { IllegalArgumentException("Receiver account not found") }

        require(sender.balance >= dto.amount) { "Insufficient funds" }

        // Обновляем балансы
        sender.balance -= dto.amount
        receiver.balance += dto.amount
        accountRepository.saveAll(listOf(sender, receiver))

        // Сохраняем транзакцию
        val transaction = transactionRepository.save(
            Transaction(
                sender = sender,
                receiver = receiver,
                amount = dto.amount
            )
        )

        // Отправляем событие
        kafkaTemplate.send(
            "transactions",
            TransactionEvent(
                transactionId = transaction.id!!,
                message = "Transfer: ${dto.amount} from ${sender.number} to ${receiver.number}"
            )
        )

        return transaction
    }
}
