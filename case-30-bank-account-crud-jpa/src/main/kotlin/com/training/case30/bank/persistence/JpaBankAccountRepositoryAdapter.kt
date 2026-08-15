package com.training.case30.bank.persistence

import com.training.case30.bank.application.BankAccountRepository
import com.training.case30.bank.domain.AccountId
import com.training.case30.bank.domain.AccountNumber
import com.training.case30.bank.domain.BankAccount
import com.training.case30.bank.domain.CustomerId
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
 // TODO(case-30): filtrer faktisk på customerId (returnerer nå alle kontoer med vilje)
 return springDataBankAccountRepository.findAll().map { it.toDomain() }
 }

 override fun existsByAccountNumber(accountNumber: AccountNumber): Boolean =
 springDataBankAccountRepository.existsByAccountNumber(accountNumber.value)

 override fun existsByCustomerId(customerId: CustomerId): Boolean =
 springDataBankAccountRepository.existsByCustomerId(customerId.value)

 override fun deleteById(id: AccountId) {
 springDataBankAccountRepository.deleteById(id.value)
 }
}
