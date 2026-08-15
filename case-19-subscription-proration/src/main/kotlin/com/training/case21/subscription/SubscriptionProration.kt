package com.training.case21.subscription

import java.math.BigDecimal
import java.time.LocalDate

data class Plan(val monthlyPrice: BigDecimal)
data class BillingPeriod(val start: LocalDate, val endInclusive: LocalDate)

class ProrationCalculator {
 fun calculateUpgradeCharge(currentPlan: Plan, targetPlan: Plan, period: BillingPeriod, changeDate: LocalDate): BigDecimal {
 TODO("Implement proration with explicit inclusive date semantics, upgrade/downgrade handling, and rounding strategy")
 }
}
