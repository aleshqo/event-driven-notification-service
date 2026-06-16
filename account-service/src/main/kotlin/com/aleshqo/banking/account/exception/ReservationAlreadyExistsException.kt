package com.aleshqo.banking.account.exception

class ReservationAlreadyExistsException(requestId: String) :
    RuntimeException("Reservation already exists: $requestId")