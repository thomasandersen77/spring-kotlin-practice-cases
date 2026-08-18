package com.training.case30.bank.domain

import java.math.BigDecimal
import java.math.RoundingMode

data class Money private constructor(val amount: BigDecimal) : Comparable<Money> {

	override fun compareTo(other: Money): Int = amount.compareTo(other.amount)

	operator fun plus(other: Money): Money = of(amount.add(other.amount))

	fun subtractSafely(other: Money): Money {
		val next = amount.subtract(other.amount)
		check(next >= BigDecimal.ZERO) { "Resulting amount cannot be negative" }
		return of(next)
	}

	companion object {
		private const val SCALE = 2
		private val ROUNDING_MODE = RoundingMode.HALF_EVEN
		val ZERO = Money(BigDecimal.ZERO.setScale(SCALE, ROUNDING_MODE))

		fun of(rawAmount: BigDecimal): Money {
			require(rawAmount >= BigDecimal.ZERO) { "Amount cannot be negative" }
			// TODO(case-30): normaliser alltid scale konsekvent her
			return Money(rawAmount)
		}

		fun ofPositive(rawAmount: BigDecimal): Money {
			val amount = of(rawAmount)
			require(amount > ZERO) { "Amount must be greater than zero" }
			return amount
		}
	}
}
