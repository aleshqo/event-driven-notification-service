package com.aleshqo.banking.account.service

import com.aleshqo.banking.account.entity.Account
import com.aleshqo.banking.account.entity.AccountTransaction
import com.aleshqo.banking.account.entity.Reservation
import com.aleshqo.banking.account.exception.AccountNotFoundException
import com.aleshqo.banking.account.exception.InsufficientFundsException
import com.aleshqo.banking.account.exception.InvalidReservationException
import com.aleshqo.banking.account.repository.AccountRepository
import com.aleshqo.banking.account.repository.AccountTransactionRepository
import com.aleshqo.banking.account.repository.ReservationRepository
import com.aleshqo.banking.common.dto.BalanceCancelRequest
import com.aleshqo.banking.common.dto.BalanceConfirmRequest
import com.aleshqo.banking.common.dto.BalanceReserveRequest
import com.aleshqo.banking.common.enums.Direction
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class BalanceService(
    private val accountRepository: AccountRepository,
    private val reservationRepository: ReservationRepository,
    private val accountTransactionRepository: AccountTransactionRepository,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun reserve(request: BalanceReserveRequest) {
        log.info("Reserve requestId={}", request.requestId)

        if (reservationRepository.existsById(request.requestId)) {
            log.info("Already reserved requestId={}", request.requestId)
            return
        }

        val account = accountRepository.findByIdOrThrow(request.accountId)
        val amount = request.amount.setScale(2)

        if (account.availableBalance < amount) {
            throw InsufficientFundsException(account.id!!)
        }

        account.availableBalance = account.availableBalance.subtract(amount)
        account.reservedBalance = account.reservedBalance.add(amount)
        accountRepository.save(account)

        reservationRepository.save(
            Reservation(
                requestId = request.requestId,
                accountId = request.accountId,
                amount = amount
            )
        )

        log.info("Reserve success requestId={}", request.requestId)
    }

    @Transactional
    fun confirm(request: BalanceConfirmRequest) {
        log.info("Confirm requestId={}", request.requestId)

        val reservation = reservationRepository.findById(request.requestId).orElse(null) ?: return
        validateReservation(reservation, request)

        val sender = accountRepository.findByIdOrThrow(request.senderId)
        val receiver = accountRepository.findByIdOrThrow(request.receiverId)
        val amount = reservation.amount

        sender.reservedBalance = sender.reservedBalance.subtract(amount)
        receiver.availableBalance = receiver.availableBalance.add(amount)

        accountRepository.save(sender)
        accountRepository.save(receiver)
        reservationRepository.delete(reservation)

        val transactionId = System.currentTimeMillis()
        accountTransactionRepository.saveAll(
            listOf(
                AccountTransaction(
                    transactionId = transactionId,
                    accountId = sender.id!!,
                    counterpartyId = receiver.id!!,
                    amount = amount,
                    direction = Direction.OUTGOING,
                    message = "Transfer ${request.requestId}"
                ),
                AccountTransaction(
                    transactionId = transactionId + 1,
                    accountId = receiver.id!!,
                    counterpartyId = sender.id!!,
                    amount = amount,
                    direction = Direction.INCOMING,
                    message = "Transfer ${request.requestId}"
                )
            )
        )

        log.info("Confirm success requestId={}", request.requestId)
    }

    @Transactional
    fun cancel(request: BalanceCancelRequest) {
        log.info("Cancel requestId={}", request.requestId)

        val reservation = reservationRepository.findById(request.requestId).orElse(null) ?: return
        if (reservation.accountId != request.accountId) {
            throw InvalidReservationException(
                "Reservation ${request.requestId} belongs to account ${reservation.accountId}, not ${request.accountId}"
            )
        }
        if (reservation.amount.compareTo(request.amount.setScale(2)) != 0) {
            throw InvalidReservationException(
                "Reservation amount ${reservation.amount} does not match request amount ${request.amount}"
            )
        }

        val account = accountRepository.findByIdOrThrow(request.accountId)
        val amount = reservation.amount

        account.reservedBalance = account.reservedBalance.subtract(amount)
        account.availableBalance = account.availableBalance.add(amount)

        accountRepository.save(account)
        reservationRepository.delete(reservation)

        log.info("Cancel success requestId={}", request.requestId)
    }

    private fun validateReservation(reservation: Reservation, request: BalanceConfirmRequest) {
        if (reservation.accountId != request.senderId) {
            throw InvalidReservationException(
                "Reservation ${request.requestId} belongs to account ${reservation.accountId}, not sender ${request.senderId}"
            )
        }
        if (reservation.amount.compareTo(request.amount.setScale(2)) != 0) {
            throw InvalidReservationException(
                "Reservation amount ${reservation.amount} does not match request amount ${request.amount}"
            )
        }
    }

    private fun AccountRepository.findByIdOrThrow(id: Long): Account =
        findById(id).orElseThrow { AccountNotFoundException(id) }
}
