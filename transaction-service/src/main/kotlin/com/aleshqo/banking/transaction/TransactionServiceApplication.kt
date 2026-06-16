package com.aleshqo.banking.transaction

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@OpenAPIDefinition(
    info = Info(
        title = "Transaction API",
        version = "1.0",
        description = "Handles money transfers between accounts"
    )
)
@SpringBootApplication
@EnableScheduling
class TransactionServiceApplication

fun main(args: Array<String>) {
    runApplication<TransactionServiceApplication>(*args)
}
