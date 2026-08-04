package com.interview.case36.bank.adapter.persistence

import com.interview.case36.bank.domain.BankAccount
import com.interview.case36.bank.domain.BankTransfer

/**
 * Explicit mapping functions between the domain model and JPA entities. Kept as extension functions,
 * the same style used for entity <-> DTO mapping in case 31, but now on the persistence side of the
 * application instead of the web side.
 */

/**
 * TODO 5: Map a persisted [AccountJpaEntity] back to the domain.
 * Hint: use [BankAccount.reconstitute] - not `BankAccount.open(...)` - since the account already exists.
 */
fun AccountJpaEntity.toDomain(): BankAccount =
    TODO("TODO 5: map AccountJpaEntity til BankAccount via BankAccount.reconstitute")

/**
 * TODO 4: Map a domain [BankAccount] to an [AccountJpaEntity].
 *
 * [existingVersion] must be the `@Version` value already stored in the database when this account is
 * being updated (0 for a brand new account). The domain has no concept of a JPA version number, so the
 * persistence adapter (TODO 6) is responsible for looking up the existing row and passing its version
 * in here - never invent a version number in this mapping function itself.
 */
fun BankAccount.toEntity(existingVersion: Long = 0): AccountJpaEntity =
    TODO("TODO 4: map BankAccount til AccountJpaEntity, bevar existingVersion")

/**
 * TODO 5: Map a persisted [TransferJpaEntity] back to the domain.
 */
fun TransferJpaEntity.toDomain(): BankTransfer =
    TODO("TODO 5: map TransferJpaEntity til BankTransfer")

/**
 * TODO 4: Map a domain [BankTransfer] to a [TransferJpaEntity]. A transfer is only ever created once
 * and never updated, so there is no version/existing-row concern here.
 */
fun BankTransfer.toEntity(): TransferJpaEntity =
    TODO("TODO 4: map BankTransfer til TransferJpaEntity")
