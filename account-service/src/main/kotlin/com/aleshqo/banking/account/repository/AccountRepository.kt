package com.aleshqo.banking.account.repository

import com.aleshqo.banking.account.entity.Account
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {

    fun findAllByOrderByAvailableBalanceDesc(pageable: Pageable): List<Account>
}
