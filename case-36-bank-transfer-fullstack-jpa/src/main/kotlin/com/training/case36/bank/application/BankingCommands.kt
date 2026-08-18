package com.training.case36.bank.application

import com.training.case36.bank.domain.AccountId
import com.training.case36.bank.domain.Money

data class CreateAccountCommand(val ownerName: String)

data class DepositCommand(val accountId: AccountId, val amount: Money)

data class TransferMoneyCommand(
	val fromAccountId: AccountId,
	val toAccountId: AccountId,
	val amount: Money,
)
