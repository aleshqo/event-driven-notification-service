package com.example.banking.account.repository

import com.example.banking.account.entity.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<Reservation, String> {

    fun findByRequestId(requestId: String): Reservation?

    fun existsByRequestId(requestId: String): Boolean
}