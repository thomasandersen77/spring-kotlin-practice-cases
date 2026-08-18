package com.training.case30.bank.persistence

import com.training.case30.bank.application.CustomerRepository
import com.training.case30.bank.domain.Customer
import com.training.case30.bank.domain.CustomerId
import org.springframework.stereotype.Repository

@Repository
class JpaCustomerRepositoryAdapter(
	private val springDataCustomerRepository: SpringDataCustomerRepository
) : CustomerRepository {

	override fun save(customer: Customer): Customer {
		val saved = springDataCustomerRepository.save(customer.toEntity())
		return saved.toDomain()
	}

	override fun findById(id: CustomerId): Customer? =
		springDataCustomerRepository.findById(id.value).orElse(null)?.toDomain()

	override fun findAll(): List<Customer> =
		springDataCustomerRepository.findAll().map { it.toDomain() }

	override fun existsByEmail(email: String): Boolean =
		springDataCustomerRepository.existsByEmail(email)

	override fun existsByEmailExcludingId(email: String, excludedId: CustomerId): Boolean =
		springDataCustomerRepository.existsByEmailAndIdNot(email, excludedId.value)

	override fun deleteById(id: CustomerId) {
		springDataCustomerRepository.deleteById(id.value)
	}
}
