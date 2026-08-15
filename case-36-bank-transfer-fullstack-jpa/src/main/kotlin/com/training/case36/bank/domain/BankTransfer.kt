package com.training.case36.bank.domain

import java.time.Instant

/**
 * A completed internal transfer between two accounts. This is a simple, immutable fact - not an
 * aggregate root - so it is a plain `data class`. It intentionally does not hold references to the two
 * [BankAccount] instances, only their [AccountId]s, so it can be persisted as a single flat row (see
 * `TransferJpaEntity`) without a bidirectional object graph.
 *
 * Same-account and non-positive-amount validation is the responsibility of the use case that creates a
 * transfer ([com.training.case36.bank.application.BankingService.transfer], TODO 12) via
 * [InvalidTransferException] - by the time a `BankTransfer` exists, it already represents something
 * that is allowed to have happened.
 */
data class BankTransfer(
 val id: TransferId,
 val fromAccountId: AccountId,
 val toAccountId: AccountId,
 val amount: Money,
 val executedAt: Instant
)
