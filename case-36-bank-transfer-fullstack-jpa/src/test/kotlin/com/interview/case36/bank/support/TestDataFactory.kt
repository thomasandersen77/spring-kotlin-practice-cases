package com.interview.case36.bank.support

import com.interview.case36.bank.domain.AccountId
import com.interview.case36.bank.domain.AccountStatus
import com.interview.case36.bank.domain.BankAccount
import com.interview.case36.bank.domain.Money
import java.math.BigDecimal

/**
 * Small, explicit test fixtures for case 36. Deliberately not a generic test framework - just a couple
 * of named helpers so test setup reads clearly. Each call produces a fresh account with a random
 * [AccountId], so tests never collide with data left behind by other tests sharing the same H2 schema.
 */
object TestDataFactory {

    fun activeAccount(
        ownerName: String = "Test Testesen",
        balanceKroner: String = "0.00"
    ): BankAccount = BankAccount.reconstitute(
        id = AccountId.new(),
        ownerName = ownerName,
        status = AccountStatus.ACTIVE,
        balance = Money.ofKroner(BigDecimal(balanceKroner))
    )

    fun blockedAccount(
        ownerName: String = "Blokkert Bruker",
        balanceKroner: String = "0.00"
    ): BankAccount = BankAccount.reconstitute(
        id = AccountId.new(),
        ownerName = ownerName,
        status = AccountStatus.BLOCKED,
        balance = Money.ofKroner(BigDecimal(balanceKroner))
    )
}
