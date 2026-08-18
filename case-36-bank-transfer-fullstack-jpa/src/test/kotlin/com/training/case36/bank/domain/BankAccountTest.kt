package com.training.case36.bank.domain

import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class BankAccountTest {

	private fun activeAccountWithBalance(balanceKroner: String): BankAccount =
		BankAccount.reconstitute(
			id = AccountId.new(),
			ownerName = "Kari Nordmann",
			status = AccountStatus.ACTIVE,
			balance = Money.ofKroner(BigDecimal(balanceKroner)),
		)

	private fun blockedAccountWithBalance(balanceKroner: String): BankAccount =
		BankAccount.reconstitute(
			id = AccountId.new(),
			ownerName = "Kari Nordmann",
			status = AccountStatus.BLOCKED,
			balance = Money.ofKroner(BigDecimal(balanceKroner)),
		)

	@Test
	fun `ny konto er aktiv med null saldo`() {
		val account = BankAccount.open("Kari Nordmann")
		assertThat(account.isActive()).isTrue()
		assertThat(account.balance).isEqualTo(Money.ZERO)
	}

	@Test
	fun `aktiv konto kan krediteres nar det er dekning for regelen`() {
		val account = BankAccount.open("Kari Nordmann")
		account.credit(Money.ofKroner(BigDecimal("500.00")))
		assertThat(account.balance.toKroner()).isEqualByComparingTo("500.00")
	}

	@Test
	fun `kreditering oker saldoen med noyaktig belopet`() {
		val account = BankAccount.open("Kari Nordmann")
		account.credit(Money.ofKroner(BigDecimal("123.45")))
		account.credit(Money.ofKroner(BigDecimal("0.55")))
		assertThat(account.balance.toKroner()).isEqualByComparingTo("124.00")
	}

	@Test
	fun `aktiv konto kan debiteres nar det er dekning`() {
		val account = activeAccountWithBalance("500.00")
		account.debit(Money.ofKroner(BigDecimal("200.00")))
		assertThat(account.balance.toKroner()).isEqualByComparingTo("300.00")
	}

	@Test
	fun `konto kan ikke overtrekkes`() {
		val account = activeAccountWithBalance("100.00")
		assertThatThrownBy { account.debit(Money.ofKroner(BigDecimal("150.00"))) }
			.isInstanceOf(InsufficientFundsException::class.java)
		assertThat(account.balance.toKroner()).isEqualByComparingTo("100.00")
	}

	@Test
	fun `saldo kan bli noyaktig null`() {
		val account = activeAccountWithBalance("100.00")
		account.debit(Money.ofKroner(BigDecimal("100.00")))
		assertThat(account.balance).isEqualTo(Money.ZERO)
	}

	@Test
	fun `blokkert konto kan ikke debiteres`() {
		val account = blockedAccountWithBalance("500.00")
		assertThatThrownBy { account.debit(Money.ofKroner(BigDecimal("10.00"))) }
			.isInstanceOf(AccountBlockedException::class.java)
		assertThat(account.balance.toKroner()).isEqualByComparingTo("500.00")
	}

	@Test
	fun `blokkert konto kan ikke krediteres`() {
		val account = blockedAccountWithBalance("0.00")
		assertThatThrownBy { account.credit(Money.ofKroner(BigDecimal("10.00"))) }
			.isInstanceOf(AccountBlockedException::class.java)
		assertThat(account.balance).isEqualTo(Money.ZERO)
	}

	@Test
	fun `debitering med null belop avvises`() {
		val account = activeAccountWithBalance("100.00")
		assertThatThrownBy { account.debit(Money.ZERO) }
			.isInstanceOf(IllegalArgumentException::class.java)
	}

	@Test
	fun `kreditering med null belop avvises`() {
		val account = BankAccount.open("Kari Nordmann")
		assertThatThrownBy { account.credit(Money.ZERO) }
			.isInstanceOf(IllegalArgumentException::class.java)
	}
}
