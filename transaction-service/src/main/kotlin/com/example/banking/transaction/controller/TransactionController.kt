package com.example.banking.transaction.controller

import com.example.banking.common.dto.TransactionDTO
import com.example.banking.transaction.entity.Transaction
import com.example.banking.transaction.service.TransactionService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val service: TransactionService
) {
    @PostMapping
    fun transfer(@RequestBody @Valid dto: TransactionDTO): Transaction {
        return service.transfer(dto)
    }
}