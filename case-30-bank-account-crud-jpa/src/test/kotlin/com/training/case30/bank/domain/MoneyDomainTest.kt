package com.training.case30.bank.domain

import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MoneyDomainTest {

	@Test
	fun `money should use consistent scale with half-even rounding`() {
		val amount = Money.of(BigDecimal("10.005"))
		assertThat(amount.amount).isEqualByComparingTo("10.00")
	}
}
