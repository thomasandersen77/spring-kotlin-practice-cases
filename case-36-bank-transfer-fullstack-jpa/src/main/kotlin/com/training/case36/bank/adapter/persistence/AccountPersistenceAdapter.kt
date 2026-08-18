package com.training.case36.bank.adapter.persistence

import com.training.case36.bank.application.port.AccountRepository
import com.training.case36.bank.domain.AccountId
import com.training.case36.bank.domain.BankAccount
import org.springframework.stereotype.Repository

/**
 * Adapts [AccountJpaRepository] (Spring Data) to the application's [AccountRepository] port. This
 * is the only class in this case that is allowed to know both the domain model and the JPA entity
 * model.
 */
@Repository
class AccountPersistenceAdapter(private val accountJpaRepository: AccountJpaRepository) :
	AccountRepository {

	/**
	 * TODO 6: Look up the entity by id and map it to the domain, or return null if it does not
	 * exist.
	 */
	override fun findById(id: AccountId): BankAccount? =
		TODO("TODO 6: sla opp AccountJpaEntity og map til domene, eller returner null")

	/**
	 * TODO 6: Save an account.
	 *
	 * If an entity with this id already exists, look it up first so the existing `@Version` value
	 * can be carried over via `toEntity(existingVersion = ...)` - otherwise Hibernate's optimistic
	 * lock check would be defeated by always writing version 0. If it does not exist yet, this is a
	 * new account (existingVersion defaults to 0).
	 */
	override fun save(account: BankAccount): BankAccount =
		TODO("TODO 6: bevar eksisterende versjon ved oppdatering, lagre og map tilbake til domene")
}
