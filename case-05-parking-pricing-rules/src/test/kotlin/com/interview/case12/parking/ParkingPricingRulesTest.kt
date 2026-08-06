package com.interview.case12.parking

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class ParkingPricingRulesTest {
    private val calculator = ParkingPriceCalculator()
    private val tariff = ParkingTariff(Money(BigDecimal("50.00")), Money(BigDecimal("120.00")))

    @Test
    fun `parking below 15 minutes should be free`() {
        val session = ParkingSession(
            vehicleType = VehicleType.CAR,
            startsAt = LocalDateTime.parse("2026-01-01T10:00:00"),
            endsAt = LocalDateTime.parse("2026-01-01T10:10:00")
        )

        val price = calculator.calculate(session, tariff)

        assertThat(price.amount).isEqualByComparingTo("0.00")
    }

    @Test
    fun `exactly 15 minutes should cost one started hour`() {
        val price = calculator.calculate(session("2026-01-01T10:00:00", "2026-01-01T10:15:00"), tariff)

        assertThat(price.amount).isEqualByComparingTo("50.00")
    }

    @Test
    fun `started hours and vehicle factors should determine ordinary price`() {
        val car = calculator.calculate(session("2026-01-01T10:00:00", "2026-01-01T11:01:00"), tariff)
        val motorcycle = calculator.calculate(
            session("2026-01-01T10:00:00", "2026-01-01T11:01:00", VehicleType.MOTORCYCLE), tariff
        )
        val ev = calculator.calculate(
            session("2026-01-01T10:00:00", "2026-01-01T11:01:00", VehicleType.EV), tariff
        )

        assertThat(car.amount).isEqualByComparingTo("100.00")
        assertThat(motorcycle.amount).isEqualByComparingTo("50.00")
        assertThat(ev.amount).isEqualByComparingTo("75.00")
    }

    @Test
    fun `night session should be capped by night maximum`() {
        val price = calculator.calculate(session("2026-01-01T22:00:00", "2026-01-02T02:00:00"), tariff)

        assertThat(price.amount).isEqualByComparingTo("120.00")
    }

    @Test
    fun `invalid interval should be rejected`() {
        assertThatThrownBy {
            calculator.calculate(session("2026-01-01T10:00:00", "2026-01-01T10:00:00"), tariff)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    private fun session(start: String, end: String, type: VehicleType = VehicleType.CAR) = ParkingSession(
        vehicleType = type,
        startsAt = LocalDateTime.parse(start),
        endsAt = LocalDateTime.parse(end)
    )
}
