package com.training.case36.bank.adapter.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

/**
 * A flat record of one completed transfer: the IDs of the two accounts involved, the amount, and
 * when it happened. Deliberately not a bidirectional JPA relationship to [AccountJpaEntity] - a
 * transfer is a fact about two accounts, not a navigable object graph, and modelling it as one
 * avoids lazy-loading surprises entirely.
 */
@Entity
@Table(name = "bank_transfers")
class TransferJpaEntity(
	@Id @Column(name = "id", nullable = false, updatable = false) var id: UUID,
	@Column(name = "from_account_id", nullable = false, updatable = false) var fromAccountId: UUID,
	@Column(name = "to_account_id", nullable = false, updatable = false) var toAccountId: UUID,
	@Column(name = "amount_ore", nullable = false, updatable = false) var amountOre: Long,
	@Column(name = "executed_at", nullable = false, updatable = false) var executedAt: Instant,
)
