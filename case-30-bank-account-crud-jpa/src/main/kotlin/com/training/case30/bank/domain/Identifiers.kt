package com.training.case30.bank.domain

import java.util.UUID

@JvmInline value class CustomerId(val value: UUID)

@JvmInline value class AccountId(val value: UUID)

@JvmInline
value class AccountNumber(val value: String) {
	init {
		require(value.isNotBlank()) { "Account number cannot be blank" }
	}
}
