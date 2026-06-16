package com.aleshqo.banking.common.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.WebRequest

object ErrorResponseFactory {

    fun build(
        status: HttpStatus,
        error: String,
        message: String?,
        errorCode: String? = null,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {

        val response = ErrorResponse(
            status = status.value(),
            error = error,
            message = message,
            errorCode = errorCode,
            path = request.getDescription(false)
        )

        return ResponseEntity(response, status)
    }
}