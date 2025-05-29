package com.example.banking.account.service

import com.example.banking.account.entity.Account
import com.example.banking.account.entity.Reservation
import com.example.banking.account.repository.AccountRepository
import com.example.banking.account.repository.ReservationRepository
import com.example.banking.common.dto.BalanceCancelRequest
import com.example.banking.common.dto.BalanceConfirmRequest
import com.example.banking.common.dto.BalanceReserveRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.RoundingMode

@Service
class BalanceService(
    private val accountRepository: AccountRepository,
    private val reservationRepository: ReservationRepository,
) {
    // Константа для округления денежных значений
    private val MONEY_SCALE = 2
    private val ROUNDING_MODE = RoundingMode.HALF_UP

    @Transactional
    fun reserve(request: BalanceReserveRequest) {
        if (reservationRepository.existsByRequestId(request.requestId)) {
            return
        }

        val account = accountRepository.findByIdOrThrow(request.accountId)
        val amount = request.amount.setScale(MONEY_SCALE, ROUNDING_MODE)

        if (account.balance.compareTo(amount) < 0) {
            throw IllegalStateException("Недостаточно средств на счёте")
        }

        account.balance = account.balance.subtract(amount)
        accountRepository.save(account)

        reservationRepository.save(
            Reservation(
                requestId = request.requestId,
                accountId = request.accountId,
                amount = amount
            )
        )
    }

    @Transactional
    fun confirm(request: BalanceConfirmRequest) {
        val reservation = reservationRepository.findByRequestId(request.requestId)
            ?: return

        val receiver = accountRepository.findByIdOrThrow(request.receiverId)
        receiver.balance = receiver.balance.add(reservation.amount)
        accountRepository.save(receiver)

        reservationRepository.delete(reservation)
    }

    @Transactional
    fun cancel(request: BalanceCancelRequest) {
        val reservation = reservationRepository.findByRequestId(request.requestId)
            ?: return

        val account = accountRepository.findByIdOrThrow(request.accountId)
        account.balance = account.balance.add(reservation.amount)
        accountRepository.save(account)

        reservationRepository.delete(reservation)
    }

    private fun AccountRepository.findByIdOrThrow(id: Long): Account =
        findById(id).orElseThrow { IllegalArgumentException("Account $id not found") }
}
