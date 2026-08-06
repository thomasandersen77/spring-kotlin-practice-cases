package com.interview.case30.bank.persistence

import com.interview.case30.bank.application.BankAccountRepository
import com.interview.case30.bank.domain.AccountId
import com.interview.case30.bank.domain.AccountNumber
import com.interview.case30.bank.domain.BankAccount
import com.interview.case30.bank.domain.CustomerId
import org.springframework.stereotype.Repository

@Repository
class JpaBankAccountRepositoryAdapter(
    private val springDataBankAccountRepository: SpringDataBankAccountRepository,
    private val springDataCustomerRepository: SpringDataCustomerRepository
) : BankAccountRepository {

    override fun save(account: BankAccount): BankAccount {
        val customer = springDataCustomerRepository.getReferenceById(account.customerId.value)
        val saved = springDataBankAccountRepository.save(account.toEntity(customer))
        return saved.toDomain()
    }

    override fun findById(id: AccountId): BankAccount? =
        springDataBankAccountRepository.findById(id.value).orElse(null)?.toDomain()

    override fun findAll(): List<BankAccount> =
        springDataBankAccountRepository.findAll().map { it.toDomain() }

    override fun findAllByCustomerId(customerId: CustomerId): List<BankAccount> {
        return springDataBankAccountRepository.findAllByCustomerId(customerId.value).map { it.toDomain() }
    }

    override fun existsByAccountNumber(accountNumber: AccountNumber): Boolean =
        springDataBankAccountRepository.existsByAccountNumber(accountNumber.value)

    override fun existsByCustomerId(customerId: CustomerId): Boolean =
        springDataBankAccountRepository.existsByCustomerId(customerId.value)

    override fun deleteById(id: AccountId) {
        springDataBankAccountRepository.deleteById(id.value)
    }
}
