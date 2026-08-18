package com.training.case36.bank.application.port

import com.training.case36.bank.domain.BankTransfer

/**
 * Port for storing completed transfers. `count()` exists mainly so tests can assert "no transfer
 * row was persisted" without depending on JPA. Implemented by
 * [com.training.case36.bank.adapter.persistence.TransferPersistenceAdapter], TODO 6.
 */
interface TransferRepository {
	fun save(transfer: BankTransfer): BankTransfer

	fun count(): Long
}
