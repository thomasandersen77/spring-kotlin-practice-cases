package com.training.case30.bank.domain

open class DomainException(
	val code: String,
	override val message: String,
) : RuntimeException(message)

class CustomerNotFoundException(customerId: CustomerId) :
	DomainException(
		code = "CUSTOMER_NOT_FOUND",
		message = "Customer ${customerId.value} was not found",
	)

class AccountNotFoundException(accountId: AccountId) :
	DomainException(
		code = "ACCOUNT_NOT_FOUND",
		message = "Account ${accountId.value} was not found",
	)

class EmailAlreadyInUseException(email: String) :
	DomainException(
		code = "EMAIL_ALREADY_IN_USE",
		message = "Email $email is already in use",
	)

class AccountNumberAlreadyExistsException(accountNumber: String) :
	DomainException(
		code = "ACCOUNT_NUMBER_ALREADY_EXISTS",
		message = "Account number $accountNumber already exists",
	)

class CustomerHasAccountsException(customerId: CustomerId) :
	DomainException(
		code = "CUSTOMER_HAS_ACCOUNTS",
		message = "Customer ${customerId.value} still has bank accounts",
	)

class AccountHasBalanceException(accountId: AccountId) :
	DomainException(
		code = "ACCOUNT_HAS_BALANCE",
		message = "Account ${accountId.value} cannot be deleted while balance is not zero",
	)

class InsufficientFundsException(accountId: AccountId) :
	DomainException(
		code = "INSUFFICIENT_FUNDS",
		message = "Account ${accountId.value} has insufficient funds",
	)

class AccountClosedException(accountId: AccountId) :
	DomainException(
		code = "ACCOUNT_CLOSED",
		message = "Account ${accountId.value} is closed",
	)
