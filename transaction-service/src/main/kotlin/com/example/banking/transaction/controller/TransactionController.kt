package com.example.banking.transaction.controller

import com.example.banking.common.dto.TransactionDTO
import com.example.banking.transaction.kafka.producer.TransactionTryProducer
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionTryProducer: TransactionTryProducer
) {

    @PostMapping
    fun initiateTransfer(@RequestBody @Valid dto: TransactionDTO): ResponseEntity<String> {
        transactionTryProducer.sendTryEvent(dto)
        return ResponseEntity.accepted().body("Try initiated")
    }
}