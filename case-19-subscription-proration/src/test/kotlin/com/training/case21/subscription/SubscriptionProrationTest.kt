package com.training.case21.subscription

import java.math.BigDecimal
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SubscriptionProrationTest {
	@Test
	fun `mid-month upgrade should charge remaining-period difference`() {
		val calculator = ProrationCalculator()
		val period =
			BillingPeriod(
				start = LocalDate.parse("2026-01-01"),
				endInclusive = LocalDate.parse("2026-01-31"),
			)

		val charge =
			calculator.calculateUpgradeCharge(
				currentPlan = Plan(BigDecimal("500.00")),
				targetPlan = Plan(BigDecimal("800.00")),
				period = period,
				changeDate = LocalDate.parse("2026-01-16"),
			)

		assertThat(charge).isGreaterThan(BigDecimal.ZERO)
	}
}
