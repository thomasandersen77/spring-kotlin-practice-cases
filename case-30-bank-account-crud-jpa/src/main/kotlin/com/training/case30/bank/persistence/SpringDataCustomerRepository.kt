package com.training.case30.bank.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataCustomerRepository : JpaRepository<CustomerEntity, UUID> {
 fun existsByEmail(email: String): Boolean
 fun existsByEmailAndIdNot(email: String, id: UUID): Boolean
}
