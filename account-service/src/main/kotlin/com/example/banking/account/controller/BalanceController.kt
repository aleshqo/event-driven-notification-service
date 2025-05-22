package com.example.banking.account.controller

import com.example.banking.account.dto.BalanceUpdateRequest
import com.example.banking.account.service.BalanceService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/accounts")
class BalanceController(
    private val balanceService: BalanceService
) {

    @PostMapping("/transfer")
    fun transfer(@RequestBody @Valid request: BalanceUpdateRequest): ResponseEntity<Void> {
        balanceService.transfer(request)
        return ResponseEntity.ok().build()
    }
}