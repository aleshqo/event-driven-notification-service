package com.aleshqo.banking.account.exception

import com.aleshqo.banking.common.exception.BusinessException
import com.aleshqo.banking.common.exception.ErrorResponse
import com.aleshqo.banking.common.exception.ErrorResponseFactory
import org.springframework.dao.OptimisticLockingFailureException
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

    @ExceptionHandler(AccountNotFoundException::class)
    fun handleAccountNotFound(
        ex: AccountNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.NOT_FOUND,
            error = "Account Not Found",
            message = ex.message ?: "Account not found",
            errorCode = "ACCOUNT_NOT_FOUND",
            request = request
        )

    @ExceptionHandler(InsufficientFundsException::class)
    fun handleInsufficientFunds(
        ex: InsufficientFundsException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.BAD_REQUEST,
            error = "Insufficient Funds",
            message = ex.message ?: "Not enough balance",
            errorCode = "INSUFFICIENT_FUNDS",
            request = request
        )

    @ExceptionHandler(ReservationAlreadyExistsException::class)
    fun handleReservationExists(
        ex: ReservationAlreadyExistsException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.CONFLICT,
            error = "Reservation Exists",
            message = ex.message ?: "Reservation already exists",
            errorCode = "RESERVATION_ALREADY_EXISTS",
            request = request
        )

    @ExceptionHandler(InvalidReservationException::class)
    fun handleInvalidReservation(
        ex: InvalidReservationException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.BAD_REQUEST,
            error = "Invalid Reservation",
            message = ex.message,
            errorCode = ex.errorCode,
            request = request
        )

    @ExceptionHandler(OptimisticLockingFailureException::class)
    fun handleOptimisticLock(
        ex: OptimisticLockingFailureException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.CONFLICT,
            error = "Concurrent Update",
            message = "Account was modified by another transaction, please retry",
            errorCode = "OPTIMISTIC_LOCK",
            request = request
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
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
    fun handleGeneral(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> =
        ErrorResponseFactory.build(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            error = "Internal Server Error",
            message = "Unexpected error occurred",
            request = request
        )
}