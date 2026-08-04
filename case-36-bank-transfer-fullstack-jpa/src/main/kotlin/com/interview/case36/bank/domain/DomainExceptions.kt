package com.interview.case36.bank.domain

/**
 * Base type for unchecked, domain-language exceptions. [code] is a stable, machine-readable identifier
 * that [com.interview.case36.bank.adapter.web.BankingExceptionHandler] maps to an HTTP status - see
 * TODO 14 there.
 */
open class BankingException(
    val code: String,
    override val message: String
) : RuntimeException(message)

class AccountNotFoundException(accountId: AccountId) : BankingException(
    code = "ACCOUNT_NOT_FOUND",
    message = "Account ${accountId.value} was not found"
)

class AccountBlockedException(accountId: AccountId) : BankingException(
    code = "ACCOUNT_BLOCKED",
    message = "Account ${accountId.value} is blocked and cannot be used in a transaction"
)

class InsufficientFundsException(accountId: AccountId) : BankingException(
    code = "INSUFFICIENT_FUNDS",
    message = "Account ${accountId.value} does not have sufficient funds for this operation"
)

/**
 * Raised for transfer requests that are invalid independent of account state - e.g. transferring to
 * the same account, or a non-positive amount. These checks are deliberately cheap: they run before any
 * repository lookup.
 */
class InvalidTransferException(message: String) : BankingException(
    code = "INVALID_TRANSFER",
    message = message
)
