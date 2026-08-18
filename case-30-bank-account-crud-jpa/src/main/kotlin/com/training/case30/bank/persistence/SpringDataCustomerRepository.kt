package com.training.case30.bank.persistence

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataCustomerRepository : JpaRepository<CustomerEntity, UUID> {
	fun existsByEmail(email: String): Boolean

	fun existsByEmailAndIdNot(email: String, id: UUID): Boolean
}
