package com.aleshqo.banking.account.exception

import com.aleshqo.banking.common.exception.BusinessException

class InsufficientFundsException(id: Long) : BusinessException(
    errorCode = "INSUFFICIENT_FUNDS",
    message = "Insufficient funds for account $id"
)
