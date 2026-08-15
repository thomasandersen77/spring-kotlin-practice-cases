package com.training.case30.bank.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class BankAccountDomainTest {

 private fun account(
 balance: Money = Money.ZERO,
 status: AccountStatus = AccountStatus.ACTIVE
 ): BankAccount = BankAccount(
 id = AccountId(UUID.randomUUID()),
 accountNumber = AccountNumber("15030012345"),
 customerId = CustomerId(UUID.randomUUID()),
 displayName = "Brukskonto",
 balance = balance,
 status = status
 )

 @Test
 fun `new account should start with zero balance`() {
 val created = account()
 assertThat(created.balance).isEqualTo(Money.ZERO)
 }

 @Test
 fun `valid deposit should increase balance`() {
 val account = account()
 account.deposit(Money.of(BigDecimal("1000.00")))
 assertThat(account.balance.amount).isEqualByComparingTo("1000.00")
 }

 @Test
 fun `deposit on zero should be rejected`() {
 val account = account()
 assertThatThrownBy { account.deposit(Money.of(BigDecimal("0.00"))) }
 .isInstanceOf(IllegalArgumentException::class.java)
 }

 @Test
 fun `negative deposit should be rejected at command value construction`() {
 assertThatThrownBy { Money.ofPositive(BigDecimal("-1.00")) }
 .isInstanceOf(IllegalArgumentException::class.java)
 }

 @Test
 fun `valid withdrawal should reduce balance`() {
 val account = account(balance = Money.of(BigDecimal("1000.00")))
 account.withdraw(Money.of(BigDecimal("250.00")))
 assertThat(account.balance.amount).isEqualByComparingTo("750.00")
 }

 @Test
 fun `withdrawal larger than balance should reject with domain exception`() {
 val account = account(balance = Money.of(BigDecimal("100.00")))
 assertThatThrownBy { account.withdraw(Money.of(BigDecimal("150.00"))) }
 .isInstanceOf(InsufficientFundsException::class.java)
 }

 @Test
 fun `balance can become exactly zero`() {
 val account = account(balance = Money.of(BigDecimal("100.00")))
 account.withdraw(Money.of(BigDecimal("100.00")))
 assertThat(account.balance).isEqualTo(Money.ZERO)
 }

 @Test
 fun `closed account should reject deposits`() {
 val account = account(status = AccountStatus.CLOSED)
 assertThatThrownBy { account.deposit(Money.of(BigDecimal("10.00"))) }
 .isInstanceOf(AccountClosedException::class.java)
 }

 @Test
 fun `closed account should reject withdrawals`() {
 val account = account(status = AccountStatus.CLOSED)
 assertThatThrownBy { account.withdraw(Money.of(BigDecimal("10.00"))) }
 .isInstanceOf(AccountClosedException::class.java)
 }

 @Test
 fun `account with non-zero balance should not be closable`() {
 val account = account(balance = Money.of(BigDecimal("1.00")))
 assertThatThrownBy { account.close() }
 .isInstanceOf(AccountHasBalanceException::class.java)
 }
}
