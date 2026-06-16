package com.aleshqo.banking.transaction.exception

import com.aleshqo.banking.common.exception.BusinessException
import com.aleshqo.banking.common.exception.ErrorResponse
import com.aleshqo.banking.common.exception.ErrorResponseFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        ex: BusinessException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.BAD_REQUEST,
            error = "Business Error",
            message = ex.message ?: "Business exception occurred",
            errorCode = ex.errorCode,
            request = request
        )

    @ExceptionHandler(BalanceUpdateClientException::class)
    fun handleClientException(
        ex: BalanceUpdateClientException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.BAD_REQUEST,
            error = "Client Error",
            message = ex.message ?: "Balance update failed",
            errorCode = "BALANCE_UPDATE_CLIENT_ERROR",
            request = request
        )

    @ExceptionHandler(BalanceUpdateServerException::class)
    fun handleServerException(
        ex: BalanceUpdateServerException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            error = "Server Error",
            message = ex.message ?: "Internal server error during balance update",
            errorCode = "BALANCE_UPDATE_SERVER_ERROR",
            request = request
        )


    @ExceptionHandler(TransactionNotFoundException::class)
    fun handleTransactionNotFound(
        ex: TransactionNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.NOT_FOUND,
            error = "Transaction Not Found",
            message = ex.message,
            errorCode = ex.errorCode,
            request = request
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.joinToString(", ") {
            "${it.field}: ${it.defaultMessage}"
        }

        return ErrorResponseFactory.build(
            status = HttpStatus.BAD_REQUEST,
            error = "Validation Error",
            message = errors,
            errorCode = "VALIDATION_FAILED",
            request = request
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            error = "Internal Server Error",
            message = "An unexpected error occurred. Please try again later.",
            request = request
        )
}