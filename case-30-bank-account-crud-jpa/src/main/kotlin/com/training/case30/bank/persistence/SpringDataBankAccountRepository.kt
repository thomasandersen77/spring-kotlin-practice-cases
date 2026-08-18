package com.training.case30.bank.persistence

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface SpringDataBankAccountRepository : JpaRepository<BankAccountEntity, UUID> {
	fun existsByAccountNumber(accountNumber: String): Boolean

	fun existsByCustomerId(customerId: UUID): Boolean

	fun findAllByCustomerId(customerId: UUID): List<BankAccountEntity>
}
