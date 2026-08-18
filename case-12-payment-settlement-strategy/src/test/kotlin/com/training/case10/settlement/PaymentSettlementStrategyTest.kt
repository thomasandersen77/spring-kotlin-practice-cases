package com.training.case10.settlement

import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PaymentSettlementStrategyTest {
	@Test
	fun `card fee should be calculated from strategy`() {
		val calculator = SettlementCalculator(emptyList())

		val result = calculator.calculateFee(PaymentMethod.CARD, Money(BigDecimal("1000.00")))

		assertThat(result.amount).isEqualByComparingTo("15.00")
	}
}
