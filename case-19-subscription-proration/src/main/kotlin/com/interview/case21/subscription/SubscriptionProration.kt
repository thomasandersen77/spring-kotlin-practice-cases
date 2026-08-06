package com.interview.case21.subscription

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Plan(val monthlyPrice: BigDecimal) {
    init { require(monthlyPrice.signum() >= 0) { "monthly price cannot be negative" } }
}
data class BillingPeriod(val start: LocalDate, val endInclusive: LocalDate) {
    init { require(!endInclusive.isBefore(start)) { "billing period end cannot be before start" } }
}

class ProrationCalculator {
    fun calculateUpgradeCharge(currentPlan: Plan, targetPlan: Plan, period: BillingPeriod, changeDate: LocalDate): BigDecimal {
        require(changeDate in period.start..period.endInclusive) { "change date must be within billing period" }
        val priceDifference = targetPlan.monthlyPrice.subtract(currentPlan.monthlyPrice)
        if (priceDifference.signum() <= 0) return BigDecimal.ZERO.setScale(2)

        val totalDays = ChronoUnit.DAYS.between(period.start, period.endInclusive) + 1
        val remainingDays = ChronoUnit.DAYS.between(changeDate, period.endInclusive) + 1
        return priceDifference
            .multiply(BigDecimal.valueOf(remainingDays))
            .divide(BigDecimal.valueOf(totalDays), 2, RoundingMode.HALF_UP)
    }
}
