package com.example.banking.transaction.client

import com.example.banking.common.dto.AccountDTO
import org.springframework.stereotype.Component

@Component
class AccountClient {

    fun getAccount(accountId: Long): AccountDTO {
        // В реальности здесь HTTP-вызов к Account Service
        return AccountDTO(
            id = accountId,
            balance = 1000.0,
            ownerName = "Test User"
        )
    }
}