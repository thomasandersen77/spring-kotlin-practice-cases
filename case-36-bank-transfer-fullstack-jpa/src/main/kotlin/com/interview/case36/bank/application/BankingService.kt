package com.interview.case36.bank.application

import com.interview.case36.bank.application.port.AccountRepository
import com.interview.case36.bank.application.port.TransferRepository
import com.interview.case36.bank.domain.AccountId
import com.interview.case36.bank.domain.BankAccount
import com.interview.case36.bank.domain.BankTransfer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

/**
 * Orchestrates the banking use cases. `BankingService` coordinates one or more [BankAccount] aggregates
 * through [accountRepository] - it never reads or writes `balance` itself. All business rules about
 * whether an operation is allowed live in the domain ([BankAccount], `Money`); this class only decides
 * *when* to call them and *when* to commit.
 */
@Service
class BankingService(
    private val accountRepository: AccountRepository,
    private val transferRepository: TransferRepository,
    private val clock: Clock
) {

    /**
     * TODO 9: Create a brand new account as a single write transaction.
     * Hint: [BankAccount.open] plus [AccountRepository.save].
     */
    @Transactional
    fun createAccount(command: CreateAccountCommand): BankAccount {
        TODO("TODO 9: opprett og lagre en ny BankAccount")
    }

    /**
     * TODO 10: Read-only use case. Throw `AccountNotFoundException` if the account does not exist.
     */
    @Transactional(readOnly = true)
    fun getAccount(accountId: AccountId): BankAccount {
        TODO("TODO 10: sla opp konto, kast AccountNotFoundException hvis den mangler")
    }

    /**
     * TODO 11: Deposit is a single write transaction around one account aggregate.
     * Hint: look up the account (reuse [getAccount]), call [BankAccount.credit], then
     * [AccountRepository.save] and return the updated account.
     */
    @Transactional
    fun deposit(command: DepositCommand): BankAccount {
        TODO("TODO 11: hent konto, krediter via domenet, lagre og returner oppdatert konto")
    }

    /**
     * TODO 12: Internal transfer between two accounts. This is the most important use case in this
     * case: it is the one place where transaction boundaries and rollback actually matter.
     *
     * Contract:
     * - Reject a transfer to the same account, and a non-positive amount, with `InvalidTransferException`
     *   - do this BEFORE any repository lookup, since it never depends on account state.
     * - Look up both accounts; a missing account is `AccountNotFoundException`.
     * - Debit the sender and credit the recipient through the domain methods
     *   ([BankAccount.debit] / [BankAccount.credit]) - never by touching a balance field directly.
     * - Save both accounts and exactly one [BankTransfer] record.
     * - The whole operation - both account saves and the transfer save - must be atomic: if anything
     *   fails after the sender has been debited (including the transfer save itself), every change made
     *   in this method must be rolled back. No half-finished transfer may ever remain in the database.
     * - Use [clock] (not `Instant.now()` directly) so tests can control `executedAt`.
     *
     * TODO: Before implementing, write down - as a comment right here - WHERE the transaction boundary
     * is and WHY `@Transactional` on this public method is the correct place for it. Specifically:
     * why does annotating a *private* method not work, and why does calling another `@Transactional`
     * method on `this` from inside this class not open a new transaction either? Both are classic
     * Spring self-invocation pitfalls - see `## Intervjuspørsmål / debrief` in the README.
     */
    @Transactional
    fun transfer(command: TransferMoneyCommand): BankTransfer {
        TODO("TODO 12: implementer atomisk overforing mellom to kontoaggregater")
    }
}
