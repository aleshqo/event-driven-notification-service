package com.aleshqo.banking.common.exception

open class BusinessException(
    val errorCode: String,
    override val message: String
) : RuntimeException(message)