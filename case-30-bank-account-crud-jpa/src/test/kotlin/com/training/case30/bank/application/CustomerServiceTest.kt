package com.training.case30.bank.application

import com.training.case30.bank.domain.Customer
import com.training.case30.bank.domain.CustomerId
import com.training.case30.bank.domain.EmailAlreadyInUseException
import java.util.UUID
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CustomerServiceTest {

	@Test
	fun `update customer should fail when email belongs to another customer`() {
		val customerRepo =
			MutableCustomerRepository().also {
				it.save(Customer(CustomerId(UUID.randomUUID()), "Ola", "ola@example.no"))
				it.save(Customer(CustomerId(UUID.randomUUID()), "Kari", "kari@example.no"))
			}
		val accountRepo = EmptyAccountRepository()
		val service = CustomerService(customerRepo, accountRepo)

		val target = customerRepo.findAll().first { it.email == "ola@example.no" }

		assertThatThrownBy {
				service.update(target.id, "Ola Nordmann", "kari@example.no")
			}
			.isInstanceOf(EmailAlreadyInUseException::class.java)
	}
}

private class MutableCustomerRepository : CustomerRepository {
	private val storage = linkedMapOf<CustomerId, Customer>()

	override fun save(customer: Customer): Customer {
		storage[customer.id] = customer
		return customer
	}

	override fun findById(id: CustomerId): Customer? = storage[id]

	override fun findAll(): List<Customer> = storage.values.toList()

	override fun existsByEmail(email: String): Boolean = storage.values.any { it.email == email }

	override fun existsByEmailExcludingId(email: String, excludedId: CustomerId): Boolean =
		storage.values.any { it.email == email && it.id != excludedId }

	override fun deleteById(id: CustomerId) {
		storage.remove(id)
	}
}

private class EmptyAccountRepository : BankAccountRepository {
	override fun save(
		account: com.training.case30.bank.domain.BankAccount
	): com.training.case30.bank.domain.BankAccount = account

	override fun findById(
		id: com.training.case30.bank.domain.AccountId
	): com.training.case30.bank.domain.BankAccount? = null

	override fun findAll(): List<com.training.case30.bank.domain.BankAccount> = emptyList()

	override fun findAllByCustomerId(
		customerId: CustomerId
	): List<com.training.case30.bank.domain.BankAccount> = emptyList()

	override fun existsByAccountNumber(
		accountNumber: com.training.case30.bank.domain.AccountNumber
	): Boolean = false

	override fun existsByCustomerId(customerId: CustomerId): Boolean = false

	override fun deleteById(id: com.training.case30.bank.domain.AccountId) {}
}
