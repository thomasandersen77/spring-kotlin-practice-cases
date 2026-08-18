package com.training.case36.bank.domain

/**
 * A [BankAccount] is either usable ([ACTIVE]) or frozen ([BLOCKED]). A blocked account cannot be
 * debited or credited - see [BankAccount.debit] and [BankAccount.credit].
 */
enum class AccountStatus {
	ACTIVE,
	BLOCKED,
}
