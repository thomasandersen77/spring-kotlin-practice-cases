package com.interview.case10.settlement

import java.math.BigDecimal
import java.math.RoundingMode

data class Money(val amount: BigDecimal) {
    init { require(amount.signum() >= 0) { "amount cannot be negative" } }
}

enum class PaymentMethod {
    CARD,
    INVOICE,
    MOBILE,
    INTERNATIONAL_CARD
}

interface SettlementFeeStrategy {
    val method: PaymentMethod
    fun feeFor(amount: Money): Money
}

class PercentageFeeStrategy(
    override val method: PaymentMethod,
    private val rate: BigDecimal,
    private val minimum: BigDecimal = BigDecimal.ZERO
) : SettlementFeeStrategy {
    override fun feeFor(amount: Money): Money = Money(
        amount.amount.multiply(rate).max(minimum).setScale(2, RoundingMode.HALF_UP)
    )
}

class FixedFeeStrategy(override val method: PaymentMethod, private val fee: BigDecimal) : SettlementFeeStrategy {
    override fun feeFor(amount: Money): Money = Money(fee.setScale(2, RoundingMode.HALF_UP))
}

class SettlementCalculator(private val strategies: List<SettlementFeeStrategy>) {
    private val strategiesByMethod = (strategies.ifEmpty { defaultStrategies() })
        .associateBy(SettlementFeeStrategy::method)

    fun calculateFee(method: PaymentMethod, amount: Money): Money {
        val strategy = strategiesByMethod[method]
            ?: throw IllegalArgumentException("No settlement strategy for $method")
        return strategy.feeFor(amount)
    }

    private companion object {
        fun defaultStrategies(): List<SettlementFeeStrategy> = listOf(
            PercentageFeeStrategy(PaymentMethod.CARD, BigDecimal("0.015")),
            FixedFeeStrategy(PaymentMethod.INVOICE, BigDecimal("25.00")),
            FixedFeeStrategy(PaymentMethod.MOBILE, BigDecimal.ZERO),
            PercentageFeeStrategy(PaymentMethod.INTERNATIONAL_CARD, BigDecimal("0.025"), BigDecimal("30.00"))
        )
    }
}
