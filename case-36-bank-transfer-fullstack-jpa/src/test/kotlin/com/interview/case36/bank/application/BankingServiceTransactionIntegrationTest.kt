package com.interview.case36.bank.application

import com.interview.case36.bank.application.port.AccountRepository
import com.interview.case36.bank.application.port.TransferRepository
import com.interview.case36.bank.domain.AccountBlockedException
import com.interview.case36.bank.domain.AccountId
import com.interview.case36.bank.domain.AccountNotFoundException
import com.interview.case36.bank.domain.BankAccount
import com.interview.case36.bank.domain.InsufficientFundsException
import com.interview.case36.bank.domain.Money
import com.interview.case36.bank.support.TestDataFactory
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

/**
 * Verifies the ACTUAL transaction boundary in [BankingService] by reading accounts back from
 * [accountRepository]/[transferRepository] AFTER the service call returns - never by inspecting the
 * in-memory objects created at the call site. If a database read after the call still shows the old
 * balance, the transaction genuinely rolled back; if it were only checked in memory, a missing or
 * misplaced `@Transactional` could go unnoticed.
 *
 * This class is deliberately NOT `@Transactional` (neither on the class nor on any test method). If it
 * were, the test's own transaction could mask a missing `@Transactional` on [BankingService.transfer] -
 * everything would appear to roll back correctly even if the service itself never opened a transaction
 * at all.
 */
@SpringBootTest
@ActiveProfiles("test")
class BankingServiceTransactionIntegrationTest {

    @Autowired
    lateinit var bankingService: BankingService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @SpyBean
    lateinit var transferRepository: TransferRepository

    private fun seed(account: BankAccount): AccountId {
        accountRepository.save(account)
        return account.id
    }

    private fun requireAccount(id: AccountId): BankAccount =
        accountRepository.findById(id) ?: error("expected account $id to exist")

    @Suppress("UNCHECKED_CAST")
    private fun <T> anyNonNull(): T {
        Mockito.any<T>()
        return null as T
    }

    @Test
    fun `vellykket overforing debiterer avsender, krediterer mottaker og lagrer en overforingsrad`() {
        val fromId = seed(TestDataFactory.activeAccount(balanceKroner = "1000.00"))
        val toId = seed(TestDataFactory.activeAccount(balanceKroner = "0.00"))
        val transfersBefore = transferRepository.count()

        bankingService.transfer(TransferMoneyCommand(fromId, toId, Money.ofKroner(BigDecimal("250.00"))))

        assertThat(requireAccount(fromId).balance.toKroner()).isEqualByComparingTo("750.00")
        assertThat(requireAccount(toId).balance.toKroner()).isEqualByComparingTo("250.00")
        assertThat(transferRepository.count()).isEqualTo(transfersBefore + 1)
    }

    @Test
    fun `utilstrekkelig dekning endrer ingen saldo og lagrer ingen overforingsrad`() {
        val fromId = seed(TestDataFactory.activeAccount(balanceKroner = "100.00"))
        val toId = seed(TestDataFactory.activeAccount(balanceKroner = "0.00"))
        val transfersBefore = transferRepository.count()

        assertThatThrownBy {
            bankingService.transfer(TransferMoneyCommand(fromId, toId, Money.ofKroner(BigDecimal("150.00"))))
        }.isInstanceOf(InsufficientFundsException::class.java)

        assertThat(requireAccount(fromId).balance.toKroner()).isEqualByComparingTo("100.00")
        assertThat(requireAccount(toId).balance.toKroner()).isEqualByComparingTo("0.00")
        assertThat(transferRepository.count()).isEqualTo(transfersBefore)
    }

    @Test
    fun `ukjent mottaker endrer ikke avsenders saldo`() {
        val fromId = seed(TestDataFactory.activeAccount(balanceKroner = "500.00"))
        val unknownToId = AccountId.new()
        val transfersBefore = transferRepository.count()

        assertThatThrownBy {
            bankingService.transfer(TransferMoneyCommand(fromId, unknownToId, Money.ofKroner(BigDecimal("50.00"))))
        }.isInstanceOf(AccountNotFoundException::class.java)

        assertThat(requireAccount(fromId).balance.toKroner()).isEqualByComparingTo("500.00")
        assertThat(transferRepository.count()).isEqualTo(transfersBefore)
    }

    @Test
    fun `blokkert avsender gir rollback`() {
        val fromId = seed(TestDataFactory.blockedAccount(balanceKroner = "500.00"))
        val toId = seed(TestDataFactory.activeAccount(balanceKroner = "0.00"))
        val transfersBefore = transferRepository.count()

        assertThatThrownBy {
            bankingService.transfer(TransferMoneyCommand(fromId, toId, Money.ofKroner(BigDecimal("50.00"))))
        }.isInstanceOf(AccountBlockedException::class.java)

        assertThat(requireAccount(fromId).balance.toKroner()).isEqualByComparingTo("500.00")
        assertThat(requireAccount(toId).balance.toKroner()).isEqualByComparingTo("0.00")
        assertThat(transferRepository.count()).isEqualTo(transfersBefore)
    }

    @Test
    fun `simulert feil ved lagring av transfer etter kontoendringer gir rollback av begge saldoer`() {
        val fromId = seed(TestDataFactory.activeAccount(balanceKroner = "1000.00"))
        val toId = seed(TestDataFactory.activeAccount(balanceKroner = "0.00"))

        doThrow(RuntimeException("Simulert lagringsfeil for transfer"))
            .`when`(transferRepository).save(anyNonNull())

        try {
            assertThatThrownBy {
                bankingService.transfer(TransferMoneyCommand(fromId, toId, Money.ofKroner(BigDecimal("250.00"))))
            }.isInstanceOf(RuntimeException::class.java)

            assertThat(requireAccount(fromId).balance.toKroner()).isEqualByComparingTo("1000.00")
            assertThat(requireAccount(toId).balance.toKroner()).isEqualByComparingTo("0.00")
        } finally {
            // Nullstill spyen sa stubbingen ikke lekker til andre tester som deler samme (cachede)
            // Spring-kontekst. @DirtiesContext er bevisst IKKE brukt her - det er reservert for den
            // dedikerte schema-lifecycle-testen (HibernateSchemaLifecycleIntegrationTest).
            Mockito.reset(transferRepository)
        }
    }
}
