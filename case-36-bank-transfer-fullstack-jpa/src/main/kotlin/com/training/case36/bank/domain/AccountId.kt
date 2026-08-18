package com.training.case36.bank.domain

import java.util.UUID

/**
 * Stable identity for a [BankAccount]. Wraps a [UUID] so the domain never depends on how an account
 * is actually stored (JPA `@Id`, HTTP path variable, etc.).
 */
@JvmInline
value class AccountId(val value: UUID) {
	companion object {
		fun new(): AccountId = AccountId(UUID.randomUUID())
	}
}
