package com.aleshqo.banking.account.exception

import com.aleshqo.banking.common.exception.BusinessException

class InvalidReservationException(message: String) : BusinessException(
    errorCode = "INVALID_RESERVATION",
    message = message
)
