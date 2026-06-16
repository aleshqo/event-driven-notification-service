package com.aleshqo.banking.account.repository

import com.aleshqo.banking.account.entity.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<Reservation, String>
