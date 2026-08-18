package com.training.case30.bank.application

import com.training.case30.bank.domain.AccountHasBalanceException
import com.training.case30.bank.domain.AccountId
import com.training.case30.bank.domain.AccountNotFoundException
import com.training.case30.bank.domain.AccountNumber
import com.training.case30.bank.domain.AccountNumberAlreadyExistsException
import com.training.case30.bank.domain.BankAccount
import com.training.case30.bank.domain.CustomerId
import com.training.case30.bank.domain.CustomerNotFoundException
import com.training.case30.bank.domain.Money
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BankAccountService(
	private val customerRepository: CustomerRepository,
	private val bankAccountRepository: BankAccountRepository,
) {
	@Transactional
	fun create(
		customerId: CustomerId,
		accountNumber: AccountNumber,
		displayName: String,
	): BankAccount {
		if (customerRepository.findById(customerId) == null) {
			throw CustomerNotFoundException(customerId)
		}
		if (bankAccountRepository.existsByAccountNumber(accountNumber)) {
			throw AccountNumberAlreadyExistsException(accountNumber.value)
		}
		val account =
			BankAccount(
				id = AccountId(UUID.randomUUID()),
				customerId = customerId,
				accountNumber = accountNumber,
				displayName = displayName,
				balance = Money.ZERO,
			)
		return bankAccountRepository.save(account)
	}

	@Transactional(readOnly = true)
	fun get(accountId: AccountId): BankAccount =
		bankAccountRepository.findById(accountId) ?: throw AccountNotFoundException(accountId)

	@Transactional(readOnly = true)
	fun list(customerId: CustomerId?): List<BankAccount> =
		customerId?.let { bankAccountRepository.findAllByCustomerId(it) }
			?: bankAccountRepository.findAll()

	@Transactional
	fun rename(accountId: AccountId, displayName: String): BankAccount {
		val account = get(accountId)
		account.rename(displayName)
		return bankAccountRepository.save(account)
	}

	@Transactional
	fun deposit(accountId: AccountId, amount: Money): BankAccount {
		val account = get(accountId)
		account.deposit(amount)
		return bankAccountRepository.save(account)
	}

	@Transactional
	fun withdraw(accountId: AccountId, amount: Money): BankAccount {
		val account = get(accountId)
		account.withdraw(amount)
		return bankAccountRepository.save(account)
	}

	@Transactional
	fun delete(accountId: AccountId) {
		val account = get(accountId)
		if (!account.canBeDeleted()) {
			throw AccountHasBalanceException(accountId)
		}
		bankAccountRepository.deleteById(accountId)
	}
}
