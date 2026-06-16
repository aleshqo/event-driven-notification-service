package com.aleshqo.banking.common.exception

import java.time.Instant

data class ErrorResponse(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val error: String,
    val message: String?,
    val errorCode: String? = null,
    val path: String? = null
)