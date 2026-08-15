package com.training.case30.bank.application

import com.training.case30.bank.domain.AccountId
import com.training.case30.bank.domain.AccountNumber
import com.training.case30.bank.domain.AccountNumberAlreadyExistsException
import com.training.case30.bank.domain.AccountStatus
import com.training.case30.bank.domain.BankAccount
import com.training.case30.bank.domain.Customer
import com.training.case30.bank.domain.CustomerId
import com.training.case30.bank.domain.CustomerNotFoundException
import com.training.case30.bank.domain.Money
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class BankAccountServiceTest {

 @Test
 fun `create account should fail when customer does not exist`() {
 val customerRepo = FakeCustomerRepository()
 val accountRepo = FakeBankAccountRepository()
 val service = BankAccountService(customerRepo, accountRepo)

 assertThatThrownBy {
 service.create(
 customerId = CustomerId(UUID.randomUUID()),
 accountNumber = AccountNumber("15030012345"),
 displayName = "Brukskonto"
 )
 }.isInstanceOf(CustomerNotFoundException::class.java)
 }

 @Test
 fun `create account should fail when account number exists`() {
 val customer = Customer(CustomerId(UUID.randomUUID()), "Ola", "ola@example.no")
 val customerRepo = FakeCustomerRepository().also { it.save(customer) }
 val accountRepo = FakeBankAccountRepository().also {
 it.saved[AccountId(UUID.randomUUID())] = BankAccount(
 id = AccountId(UUID.randomUUID()),
 accountNumber = AccountNumber("15030012345"),
 customerId = customer.id,
 displayName = "Gamlekonto",
 balance = Money.ZERO,
 status = AccountStatus.ACTIVE
 )
 }
 val service = BankAccountService(customerRepo, accountRepo)

 assertThatThrownBy {
 service.create(
 customerId = customer.id,
 accountNumber = AccountNumber("15030012345"),
 displayName = "Ny konto"
 )
 }.isInstanceOf(AccountNumberAlreadyExistsException::class.java)
 }

 @Test
 fun `withdraw with insufficient funds should not persist changes`() {
 val customer = Customer(CustomerId(UUID.randomUUID()), "Ola", "ola@example.no")
 val account = BankAccount(
 id = AccountId(UUID.randomUUID()),
 accountNumber = AccountNumber("15030012345"),
 customerId = customer.id,
 displayName = "Brukskonto",
 balance = Money.of(BigDecimal("50.00"))
 )

 val customerRepo = FakeCustomerRepository().also { it.save(customer) }
 val accountRepo = FakeBankAccountRepository().also { it.saved[account.id] = account }
 val service = BankAccountService(customerRepo, accountRepo)

 assertThatThrownBy {
 service.withdraw(account.id, Money.of(BigDecimal("100.00")))
 }.isInstanceOf(RuntimeException::class.java)

 assertThat(accountRepo.saveCalls).isZero()
 }

 @Test
 fun `list by customer should only return accounts for selected customer`() {
 val customerA = CustomerId(UUID.randomUUID())
 val customerB = CustomerId(UUID.randomUUID())
 val repo = FakeBankAccountRepository().also {
 it.saved[AccountId(UUID.randomUUID())] = BankAccount(
 id = AccountId(UUID.randomUUID()),
 accountNumber = AccountNumber("15030000001"),
 customerId = customerA,
 displayName = "A-konto"
 )
 it.saved[AccountId(UUID.randomUUID())] = BankAccount(
 id = AccountId(UUID.randomUUID()),
 accountNumber = AccountNumber("15030000002"),
 customerId = customerB,
 displayName = "B-konto"
 )
 }
 val customerRepo = FakeCustomerRepository().also {
 it.save(Customer(customerA, "A", "a@example.no"))
 it.save(Customer(customerB, "B", "b@example.no"))
 }
 val service = BankAccountService(customerRepo, repo)

 val result = service.list(customerA)
 assertThat(result).hasSize(1)
 assertThat(result.single().customerId).isEqualTo(customerA)
 }
}

private class FakeCustomerRepository : CustomerRepository {
 private val customers = mutableMapOf<CustomerId, Customer>()

 override fun save(customer: Customer): Customer {
 customers[customer.id] = customer
 return customer
 }

 override fun findById(id: CustomerId): Customer? = customers[id]

 override fun findAll(): List<Customer> = customers.values.toList()

 override fun existsByEmail(email: String): Boolean = customers.values.any { it.email == email }

 override fun existsByEmailExcludingId(email: String, excludedId: CustomerId): Boolean =
 customers.values.any { it.email == email && it.id != excludedId }

 override fun deleteById(id: CustomerId) {
 customers.remove(id)
 }
}

private class FakeBankAccountRepository : BankAccountRepository {
 val saved = mutableMapOf<AccountId, BankAccount>()
 var saveCalls: Int = 0

 override fun save(account: BankAccount): BankAccount {
 saveCalls++
 saved[account.id] = account
 return account
 }

 override fun findById(id: AccountId): BankAccount? = saved[id]

 override fun findAll(): List<BankAccount> = saved.values.toList()

 override fun findAllByCustomerId(customerId: CustomerId): List<BankAccount> =
 saved.values.toList() // TODO(case-30 test): bevisst feil i fake for å drive implementasjon

 override fun existsByAccountNumber(accountNumber: AccountNumber): Boolean =
 saved.values.any { it.accountNumber == accountNumber }

 override fun existsByCustomerId(customerId: CustomerId): Boolean =
 saved.values.any { it.customerId == customerId }

 override fun deleteById(id: AccountId) {
 saved.remove(id)
 }
}
