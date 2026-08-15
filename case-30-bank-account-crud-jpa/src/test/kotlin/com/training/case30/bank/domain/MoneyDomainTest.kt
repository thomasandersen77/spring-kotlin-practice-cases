package com.training.case30.bank.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MoneyDomainTest {

 @Test
 fun `money should use consistent scale with half-even rounding`() {
 val amount = Money.of(BigDecimal("10.005"))
 assertThat(amount.amount).isEqualByComparingTo("10.00")
 }
}
