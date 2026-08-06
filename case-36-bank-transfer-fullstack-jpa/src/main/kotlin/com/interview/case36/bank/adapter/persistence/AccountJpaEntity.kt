package com.interview.case36.bank.adapter.persistence

import com.interview.case36.bank.domain.AccountStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.util.UUID

/**
 * JPA representation of a [com.interview.case36.bank.domain.BankAccount]. Deliberately not a Kotlin
 * `data class` - it is a mutable, framework-managed entity, and an auto-generated `equals`/`hashCode`
 * would behave badly together with Hibernate proxies and lazy state.
 *
 * `balanceOre` mirrors the domain's `Money.amountOre`: a whole number of øre, never a `Double`.
 *
 * Note on database-level protection: a `CHECK (balance_ore >= 0)` constraint was deliberately left out
 * here. Hibernate DDL generation for check constraints on H2 is workable but adds a second place where
 * the "no negative balance" rule is expressed, for limited extra safety in a single-process training
 * case with no direct SQL access path. The domain ([BankAccount.debit]) is the primary and sufficient
 * guard for this case; see the README's "H2 og Hibernate schema lifecycle" section for the full
 * reasoning and what you would do differently in a production schema owned by Flyway/Liquibase.
 */
@Entity
@Table(name = "bank_accounts")
class AccountJpaEntity(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    var id: UUID,

    @Column(name = "owner_name", nullable = false)
    var ownerName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    var status: AccountStatus,

    @Column(name = "balance_ore", nullable = false)
    var balanceOre: Long,

    @Version
    @Column(name = "version", nullable = false)
    var version: Long = 0
)
