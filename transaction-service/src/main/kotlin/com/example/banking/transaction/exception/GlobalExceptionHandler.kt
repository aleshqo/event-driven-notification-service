package com.example.banking.transaction.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

//TODO: issue#1 обработка ошибок
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BalanceUpdateClientException::class)
    fun handleClientException(ex: BalanceUpdateClientException):ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body( "Balance Update Failed, reason: ${ex.message} ")
    }

    @ExceptionHandler(BalanceUpdateServerException::class)
    fun handleServerException(ex: BalanceUpdateServerException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body( "Balance Update Failed, reason: ${ex.message} ")
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneral(ex: Exception): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: ${ex.message}")

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(
            "Invalid input: ${
                ex.bindingResult?.allErrors?.joinToString(
                    ", "
                ) { it.defaultMessage ?: "" }
            }"
        )
    }
}