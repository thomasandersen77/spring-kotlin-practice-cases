package com.training.case12.parking

import java.math.BigDecimal
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ParkingPricingRulesTest {
	@Test
	fun `parking below 15 minutes should be free`() {
		val calculator = ParkingPriceCalculator()
		val tariff =
			ParkingTariff(
				hourlyRate = Money(BigDecimal("50.00")),
				nightMax = Money(BigDecimal("120.00")),
			)
		val session =
			ParkingSession(
				vehicleType = VehicleType.CAR,
				startsAt = LocalDateTime.parse("2026-01-01T10:00:00"),
				endsAt = LocalDateTime.parse("2026-01-01T10:10:00"),
			)

		val price = calculator.calculate(session, tariff)

		assertThat(price.amount).isEqualByComparingTo("0.00")
	}
}
