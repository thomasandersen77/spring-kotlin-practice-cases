package com.interview.case36.bank.domain

import java.math.BigDecimal

/**
 * Money represents an amount of Norwegian kroner (NOK), stored internally as a whole number of øre
 * (1/100 krone) so that arithmetic never touches floating point. [Double] is never used for money in
 * this case.
 *
 * Design decision: Money can never be negative. A single non-negative invariant is enough to cover
 * both use cases in this domain - an account balance can never go negative, and a transfer amount can
 * never be negative - so there is no need for two separate money-like types. If this domain ever needed
 * genuinely negative amounts (e.g. a signed ledger entry), that would be a different type.
 *
 * TODO 1: Implement the two conversion functions below. They are the "central" factory/validation
 * functions for this type - the API receives and returns kroner as [BigDecimal] with two decimals,
 * while the domain and the database only ever see whole øre as a [Long]. The mapping between the two
 * must be explicit and exact.
 */
@JvmInline
value class Money private constructor(val amountOre: Long) : Comparable<Money> {

    override fun compareTo(other: Money): Int = amountOre.compareTo(other.amountOre)

    /** Adds two amounts. The result is always >= 0 because both operands already are. */
    operator fun plus(other: Money): Money = Money(amountOre + other.amountOre)

    /**
     * Subtracts [other] from this amount.
     *
     * @throws IllegalArgumentException if the result would be negative. This is a last-resort
     * invariant guard - callers that need to know up front whether there is enough money should use
     * [isLessThan] before subtracting (see [BankAccount.debit]).
     */
    operator fun minus(other: Money): Money {
        val resultOre = amountOre - other.amountOre
        require(resultOre >= 0) { "Resulting amount cannot be negative" }
        return Money(resultOre)
    }

    fun isLessThan(other: Money): Boolean = amountOre < other.amountOre

    /**
     * TODO 1: Convert this amount back to kroner with exactly two decimals, e.g. 25_000 øre -> 250.00,
     * 1 øre -> 0.01. The conversion must be deterministic: the same øre value always produces the same
     * [BigDecimal], with scale exactly 2, and no rounding is ever needed since øre is already the
     * smallest unit of NOK.
     */
    fun toKroner(): BigDecimal =
        TODO("TODO 1: konverter amountOre til BigDecimal kroner med skala 2")

    companion object {
        val ZERO = Money(0)

        /**
         * Creates a [Money] directly from a whole number of øre. Used by persistence mapping, where the
         * database already stores balances and transfer amounts as a `Long` number of øre.
         *
         * @throws IllegalArgumentException if [amountOre] is negative.
         */
        fun ofOre(amountOre: Long): Money {
            require(amountOre >= 0) { "Amount in øre cannot be negative" }
            return Money(amountOre)
        }

        /**
         * TODO 1: Creates a [Money] from a kroner amount received from the API (JSON -> [BigDecimal]).
         *
         * Contract:
         * - [amountKroner] must not be negative.
         * - [amountKroner] must not require more than two decimal places (e.g. 10.005 is invalid;
         *   10.50 and 10 are valid).
         * - The conversion to øre must be exact: 250.00 -> 25_000, 250.5 -> 25_050, 0.01 -> 1.
         *
         * @throws IllegalArgumentException if the amount is negative or has more than two decimals.
         */
        fun ofKroner(amountKroner: BigDecimal): Money =
            TODO("TODO 1: valider ikke-negativt belop og maks to desimaler, konverter eksakt til ore")
    }
}
