package com.interview.case16.energy

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class EnergyTariffBillingTest {
    @Test
    fun `simple consumption should produce bill`() {
        val calculator = EnergyBillCalculator()
        val tariff = EnergyTariff(
            spotPerKwh = BigDecimal("1.20"),
            gridPerKwh = BigDecimal("0.45"),
            fixedMonthly = BigDecimal("99.00")
        )

        val bill = calculator.calculate(
            start = MeterReading(BigDecimal("1000.0")),
            end = MeterReading(BigDecimal("1100.0")),
            tariff = tariff,
            vatRate = BigDecimal("0.25")
        )

        assertThat(bill.consumptionKwh).isEqualByComparingTo("100.0")
        assertThat(bill.variableCost).isEqualByComparingTo("165.00")
        assertThat(bill.vat).isEqualByComparingTo("66.0000")
        assertThat(bill.total).isEqualByComparingTo("330.00")
    }

    @Test
    fun `total should round once to øre with half up`() {
        val bill = EnergyBillCalculator().calculate(
            MeterReading(BigDecimal.ZERO), MeterReading(BigDecimal.ONE),
            EnergyTariff(BigDecimal("0.333"), BigDecimal.ZERO, BigDecimal.ZERO),
            BigDecimal("0.25")
        )

        assertThat(bill.total).isEqualByComparingTo("0.42")
    }

    @Test
    fun `backwards meter and invalid vat should be rejected`() {
        val calculator = EnergyBillCalculator()
        val tariff = EnergyTariff(BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.ZERO)

        assertThatThrownBy {
            calculator.calculate(MeterReading(BigDecimal.TEN), MeterReading(BigDecimal.ONE), tariff, BigDecimal("0.25"))
        }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy {
            calculator.calculate(MeterReading(BigDecimal.ZERO), MeterReading(BigDecimal.ZERO), tariff, BigDecimal("1.01"))
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
