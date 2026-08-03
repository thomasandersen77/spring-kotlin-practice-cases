package com.interview.case36.bank.domain

import java.util.UUID

/**
 * Stable identity for a completed [BankTransfer].
 */
@JvmInline
value class TransferId(val value: UUID) {
    companion object {
        fun new(): TransferId = TransferId(UUID.randomUUID())
    }
}
