package com.aleshqo.banking.account.service

import com.aleshqo.banking.account.repository.AccountRepository
import com.aleshqo.banking.account.repository.AccountTransactionRepository
import com.aleshqo.banking.account.repository.ReservationRepository
import com.aleshqo.banking.common.dto.BalanceConfirmRequest
import com.aleshqo.banking.common.dto.BalanceReserveRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class BalanceServiceIntegrationTest {

    companion object {
        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer<Nothing>("postgres:15").apply {
            withDatabaseName("account_db")
            withUsername("banking_user")
            withPassword("banking_pass")
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired lateinit var balanceService: BalanceService
    @Autowired lateinit var accountRepository: AccountRepository
    @Autowired lateinit var reservationRepository: ReservationRepository
    @Autowired lateinit var accountTransactionRepository: AccountTransactionRepository

    @BeforeEach
    fun clean() {
        accountTransactionRepository.deleteAll()
        reservationRepository.deleteAll()
        accountRepository.deleteAll()
        accountRepository.save(
            com.aleshqo.banking.account.entity.Account(
                number = "ACC-001",
                ownerName = "Alice",
                availableBalance = BigDecimal("1000.00"),
                reservedBalance = BigDecimal.ZERO
            )
        )
        accountRepository.save(
            com.aleshqo.banking.account.entity.Account(
                number = "ACC-002",
                ownerName = "Bob",
                availableBalance = BigDecimal("500.00"),
                reservedBalance = BigDecimal.ZERO
            )
        )
    }

    @Test
    @Tag("integration")
    fun `reserve and confirm transfer updates balances and writes ledger`() {
        val sender = accountRepository.findAll().first { it.number == "ACC-001" }
        val receiver = accountRepository.findAll().first { it.number == "ACC-002" }

        balanceService.reserve(BalanceReserveRequest(sender.id!!, BigDecimal("100.00"), "it-req-1"))
        balanceService.confirm(
            BalanceConfirmRequest(sender.id!!, receiver.id!!, BigDecimal("100.00"), "it-req-1")
        )

        val updatedSender = accountRepository.findById(sender.id!!).get()
        val updatedReceiver = accountRepository.findById(receiver.id!!).get()

        assertEquals(BigDecimal("900.00"), updatedSender.availableBalance)
        assertEquals(BigDecimal.ZERO.setScale(2), updatedSender.reservedBalance)
        assertEquals(BigDecimal("600.00"), updatedReceiver.availableBalance)
        assertEquals(2, accountTransactionRepository.count())
    }
}
