package com.example.banking.account.repository

import com.example.banking.account.entity.Account
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository : JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByIdForUpdate(@Param("id") id: Long): Account?
}