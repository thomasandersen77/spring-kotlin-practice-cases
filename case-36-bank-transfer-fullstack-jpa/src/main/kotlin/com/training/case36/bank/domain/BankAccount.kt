package com.training.case36.bank.domain

/**
 * Aggregate root for a single bank account. `BankAccount` owns every rule about its own state:
 * whether it is active, whether a withdrawal is allowed, and how its balance changes. No other class
 * - not the application service, not the controller, not the persistence adapter - is allowed to set
 * [balance] directly. That is why [balance] has a private setter: the only way to change it is through
 * [credit] and [debit].
 *
 * The constructor is private. Use [open] to create a brand new account, or [reconstitute] to rebuild an
 * existing account from persistence (see `PersistenceMapping.kt`, TODO 5).
 */
class BankAccount private constructor(
 val id: AccountId,
 val ownerName: String,
 status: AccountStatus,
 balance: Money
) {
 var status: AccountStatus = status
 private set

 var balance: Money = balance
 private set

 fun isActive(): Boolean = status == AccountStatus.ACTIVE

 /**
 * TODO 2: Credit this account (used for deposits and for the receiving side of a transfer).
 *
 * Contract:
 * - `amount` must be greater than [Money.ZERO], otherwise `IllegalArgumentException`.
 * - The account must be [AccountStatus.ACTIVE]; otherwise throw [AccountBlockedException].
 * - On success, [balance] increases by exactly `amount`.
 */
 fun credit(amount: Money) {
 TODO("TODO 2: implementer kreditering med regler for blokkert konto og positivt belop")
 }

 /**
 * TODO 3: Debit this account (used for the sending side of a transfer).
 *
 * Contract:
 * - `amount` must be greater than [Money.ZERO], otherwise `IllegalArgumentException`.
 * - The account must be [AccountStatus.ACTIVE]; otherwise throw [AccountBlockedException].
 * - The account must have sufficient funds; otherwise throw [InsufficientFundsException]. Check
 * this explicitly (e.g. with [Money.isLessThan]) rather than relying on the exception that
 * [Money.minus] happens to throw - that exception carries no account context.
 * - On success, [balance] decreases by exactly `amount`.
 */
 fun debit(amount: Money) {
 TODO("TODO 3: implementer debitering med regler for blokkert konto og manglende dekning")
 }

 companion object {
 /** Opens a brand new, active account with a zero balance. */
 fun open(ownerName: String): BankAccount {
 require(ownerName.isNotBlank()) { "Owner name cannot be blank" }
 return BankAccount(
 id = AccountId.new(),
 ownerName = ownerName.trim(),
 status = AccountStatus.ACTIVE,
 balance = Money.ZERO
 )
 }

 /**
 * Rebuilds an already-existing account, e.g. when mapping an [com.training.case36.bank.adapter.persistence.AccountJpaEntity]
 * back to the domain. Unlike [open], this does not force a zero starting balance or ACTIVE status.
 */
 fun reconstitute(id: AccountId, ownerName: String, status: AccountStatus, balance: Money): BankAccount {
 require(ownerName.isNotBlank()) { "Owner name cannot be blank" }
 return BankAccount(id, ownerName, status, balance)
 }
 }
}
