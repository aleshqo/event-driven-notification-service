package com.aleshqo.banking.transaction.controller

import com.aleshqo.banking.common.dto.TransactionDTO
import com.aleshqo.banking.common.dto.TransactionResponse
import com.aleshqo.banking.transaction.kafka.producer.TransactionTryProducer
import com.aleshqo.banking.transaction.service.TransactionQueryService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionTryProducer: TransactionTryProducer,
    private val transactionQueryService: TransactionQueryService
) {

    @PostMapping
    fun initiateTransfer(@RequestBody @Valid dto: TransactionDTO): ResponseEntity<TransactionResponse> {
        val requestId = transactionTryProducer.sendTryEvent(dto)
        return ResponseEntity
            .accepted()
            .location(URI.create("/api/transactions/$requestId"))
            .body(
                TransactionResponse(
                    requestId = requestId,
                    senderId = dto.senderId,
                    receiverId = dto.receiverId,
                    amount = dto.amount,
                    status = com.aleshqo.banking.common.enums.TransactionStatus.PENDING,
                    timestamp = java.time.Instant.now()
                )
            )
    }

    @GetMapping("/{requestId}")
    fun getTransaction(@PathVariable requestId: String): TransactionResponse =
        transactionQueryService.getByRequestId(requestId)
}
