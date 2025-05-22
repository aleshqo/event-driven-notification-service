package com.example.banking.notification.service

import com.example.banking.common.event.TransactionCompletedEvent
import com.example.banking.common.event.TransactionStatus
import org.springframework.stereotype.Service

@Service
class NotificationService {

    fun sendNotification(event: TransactionCompletedEvent) {
        when (event.status) {
            TransactionStatus.SUCCESS -> {
                println("Transaction ${event.transactionId} successful: ${event.message}")
                // Здесь можно вызывать Email/SMS/push сервис
            }
            TransactionStatus.FAILED -> {
                println("Transaction ${event.transactionId} failed: ${event.message}")
                // Аналогично: можно логировать, отправить alert и т.д.
            }
        }
    }
}