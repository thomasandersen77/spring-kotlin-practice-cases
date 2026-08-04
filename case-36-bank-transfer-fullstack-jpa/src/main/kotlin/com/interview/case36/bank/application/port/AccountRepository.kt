package com.interview.case36.bank.application.port

import com.interview.case36.bank.domain.AccountId
import com.interview.case36.bank.domain.BankAccount

/**
 * Port that [com.interview.case36.bank.application.BankingService] depends on instead of depending on
 * Spring Data directly (Dependency Inversion Principle). Implemented by
 * [com.interview.case36.bank.adapter.persistence.AccountPersistenceAdapter], TODO 6.
 */
interface AccountRepository {
    fun findById(id: AccountId): BankAccount?
    fun save(account: BankAccount): BankAccount
}
