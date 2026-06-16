package com.aleshqo.banking.account.exception

import com.aleshqo.banking.common.exception.BusinessException

class AccountNotFoundException(id: Long) : BusinessException(
    errorCode = "ACCOUNT_NOT_FOUND",
    message = "Account $id not found"
)