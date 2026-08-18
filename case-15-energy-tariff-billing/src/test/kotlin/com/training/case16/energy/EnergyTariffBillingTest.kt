package com.training.case16.energy

import java.math.BigDecimal
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EnergyTariffBillingTest {
	@Test
	fun `simple consumption should produce bill`() {
		val calculator = EnergyBillCalculator()
		val tariff =
			EnergyTariff(
				spotPerKwh = BigDecimal("1.20"),
				gridPerKwh = BigDecimal("0.45"),
				fixedMonthly = BigDecimal("99.00"),
			)

		val bill =
			calculator.calculate(
				start = MeterReading(BigDecimal("1000.0")),
				end = MeterReading(BigDecimal("1100.0")),
				tariff = tariff,
				vatRate = BigDecimal("0.25"),
			)

		assertThat(bill.total).isGreaterThan(BigDecimal.ZERO)
	}
}
