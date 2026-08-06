package com.interview.case10.settlement

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PaymentSettlementStrategyTest {
    @Test
    fun `card fee should be calculated from strategy`() {
        val calculator = SettlementCalculator(emptyList())

        val result = calculator.calculateFee(PaymentMethod.CARD, Money(BigDecimal("1000.00")))

        assertThat(result.amount).isEqualByComparingTo("15.00")
    }

    @Test
    fun `default strategies should cover fixed zero and minimum fees`() {
        val calculator = SettlementCalculator(emptyList())

        assertThat(calculator.calculateFee(PaymentMethod.INVOICE, Money(BigDecimal("100"))).amount)
            .isEqualByComparingTo("25.00")
        assertThat(calculator.calculateFee(PaymentMethod.MOBILE, Money(BigDecimal("100"))).amount)
            .isEqualByComparingTo("0.00")
        assertThat(calculator.calculateFee(PaymentMethod.INTERNATIONAL_CARD, Money(BigDecimal("100"))).amount)
            .isEqualByComparingTo("30.00")
    }

    @Test
    fun `calculator should select injected strategy and reject missing method`() {
        val custom = object : SettlementFeeStrategy {
            override val method = PaymentMethod.CARD
            override fun feeFor(amount: Money) = Money(BigDecimal("7.00"))
        }
        val calculator = SettlementCalculator(listOf(custom))

        assertThat(calculator.calculateFee(PaymentMethod.CARD, Money(BigDecimal.TEN)).amount)
            .isEqualByComparingTo("7.00")
        assertThatThrownBy { calculator.calculateFee(PaymentMethod.MOBILE, Money(BigDecimal.TEN)) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
