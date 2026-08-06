package com.interview.case21.subscription

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class SubscriptionProrationTest {
    @Test
    fun `mid-month upgrade should charge remaining-period difference`() {
        val calculator = ProrationCalculator()
        val period = BillingPeriod(
            start = LocalDate.parse("2026-01-01"),
            endInclusive = LocalDate.parse("2026-01-31")
        )

        val charge = calculator.calculateUpgradeCharge(
            currentPlan = Plan(BigDecimal("500.00")),
            targetPlan = Plan(BigDecimal("800.00")),
            period = period,
            changeDate = LocalDate.parse("2026-01-16")
        )

        assertThat(charge).isEqualByComparingTo("154.84")
    }

    @Test
    fun `first and last day should both be included`() {
        val calculator = ProrationCalculator()
        val period = BillingPeriod(LocalDate.parse("2026-01-01"), LocalDate.parse("2026-01-31"))
        val current = Plan(BigDecimal("500.00"))
        val target = Plan(BigDecimal("800.00"))

        assertThat(calculator.calculateUpgradeCharge(current, target, period, period.start))
            .isEqualByComparingTo("300.00")
        assertThat(calculator.calculateUpgradeCharge(current, target, period, period.endInclusive))
            .isEqualByComparingTo("9.68")
    }

    @Test
    fun `same price and downgrade should have zero upgrade charge`() {
        val calculator = ProrationCalculator()
        val period = BillingPeriod(LocalDate.parse("2026-02-01"), LocalDate.parse("2026-02-28"))
        val date = LocalDate.parse("2026-02-10")

        assertThat(calculator.calculateUpgradeCharge(Plan(BigDecimal("500")), Plan(BigDecimal("500")), period, date))
            .isEqualByComparingTo("0.00")
        assertThat(calculator.calculateUpgradeCharge(Plan(BigDecimal("800")), Plan(BigDecimal("500")), period, date))
            .isEqualByComparingTo("0.00")
    }

    @Test
    fun `invalid period and change date outside period should be rejected`() {
        assertThatThrownBy {
            BillingPeriod(LocalDate.parse("2026-02-02"), LocalDate.parse("2026-02-01"))
        }.isInstanceOf(IllegalArgumentException::class.java)

        val period = BillingPeriod(LocalDate.parse("2026-02-01"), LocalDate.parse("2026-02-28"))
        assertThatThrownBy {
            ProrationCalculator().calculateUpgradeCharge(
                Plan(BigDecimal("500")), Plan(BigDecimal("800")), period, LocalDate.parse("2026-03-01")
            )
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
