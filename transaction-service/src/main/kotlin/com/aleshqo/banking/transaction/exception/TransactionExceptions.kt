package com.aleshqo.banking.transaction.exception

import com.aleshqo.banking.common.exception.BusinessException

class AccountServiceException(
    message: String,
    errorCode: String = "ACCOUNT_SERVICE_ERROR"
) : BusinessException(errorCode, message)

class TransactionNotFoundException(requestId: String) : BusinessException(
    "TRANSACTION_NOT_FOUND",
    "Transaction with requestId $requestId not found"
)

class InvalidTransactionStateException(requestId: String, currentStatus: String) : BusinessException(
    "INVALID_TRANSACTION_STATE",
    "Transaction $requestId is in invalid state: $currentStatus"
)