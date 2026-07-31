package com.interview.case30.bank.persistence

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SpringDataBankAccountRepository : JpaRepository<BankAccountEntity, UUID> {
    fun existsByAccountNumber(accountNumber: String): Boolean
    fun existsByCustomerId(customerId: UUID): Boolean
    fun findAllByCustomerId(customerId: UUID): List<BankAccountEntity>
}
