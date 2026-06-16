package com.aleshqo.banking.account.controller

import com.aleshqo.banking.account.service.BalanceService
import com.aleshqo.banking.common.dto.BalanceCancelRequest
import com.aleshqo.banking.common.dto.BalanceConfirmRequest
import com.aleshqo.banking.common.dto.BalanceReserveRequest
import jakarta.validation.Valid
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
    fun reserve(@RequestBody @Valid request: BalanceReserveRequest) {
        balanceService.reserve(request)
    }

    @PostMapping("/confirm")
    fun confirm(@RequestBody @Valid request: BalanceConfirmRequest) {
        balanceService.confirm(request)
    }

    @PostMapping("/cancel")
    fun cancel(@RequestBody @Valid request: BalanceCancelRequest) {
        balanceService.cancel(request)
    }
}