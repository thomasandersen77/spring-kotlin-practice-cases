package com.interview.case30.bank.application

import com.interview.case30.bank.domain.AccountId
import com.interview.case30.bank.domain.AccountNumber
import com.interview.case30.bank.domain.BankAccount
import com.interview.case30.bank.domain.CustomerId

interface BankAccountRepository {
    fun save(account: BankAccount): BankAccount
    fun findById(id: AccountId): BankAccount?
    fun findAll(): List<BankAccount>
    fun findAllByCustomerId(customerId: CustomerId): List<BankAccount>
    fun existsByAccountNumber(accountNumber: AccountNumber): Boolean
    fun existsByCustomerId(customerId: CustomerId): Boolean
    fun deleteById(id: AccountId)
}
