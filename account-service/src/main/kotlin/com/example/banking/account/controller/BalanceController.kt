package com.example.banking.account.controller

import com.example.banking.account.service.BalanceService
import com.example.banking.common.dto.BalanceCancelRequest
import com.example.banking.common.dto.BalanceConfirmRequest
import com.example.banking.common.dto.BalanceReserveRequest
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

    @PostMapping("/reserve")
    fun reserve(@RequestBody request: BalanceReserveRequest): ResponseEntity<Void> {
        balanceService.reserve(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/confirm")
    fun confirm(@RequestBody request: BalanceConfirmRequest): ResponseEntity<Void> {
        balanceService.confirm(request)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/cancel")
    fun cancel(@RequestBody request: BalanceCancelRequest): ResponseEntity<Void> {
        balanceService.cancel(request)
        return ResponseEntity.ok().build()
    }
}