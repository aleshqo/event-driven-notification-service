package com.example.banking.transaction.controller

import com.example.banking.common.dto.TransactionDTO
import com.example.banking.transaction.kafka.TransactionRequestProducer
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val producer: TransactionRequestProducer
) {
    @PostMapping
    fun requestTransfer(@RequestBody @Valid dto: TransactionDTO): ResponseEntity<String> {
        producer.sendTransferRequest(dto)
        return ResponseEntity.accepted().body("Transaction request accepted")
    }
}