package com.training.case10.settlement

import java.math.BigDecimal

data class Money(val amount: BigDecimal)

enum class PaymentMethod {
	CARD,
	INVOICE,
	MOBILE,
	INTERNATIONAL_CARD,
}

interface SettlementFeeStrategy {
	val method: PaymentMethod

	fun feeFor(amount: Money): Money
}

class SettlementCalculator(private val strategies: List<SettlementFeeStrategy>) {
	fun calculateFee(method: PaymentMethod, amount: Money): Money {
		TODO(
			"Implement strategy lookup with explicit behavior for missing strategy and predictable fee calculation"
		)
	}
}
