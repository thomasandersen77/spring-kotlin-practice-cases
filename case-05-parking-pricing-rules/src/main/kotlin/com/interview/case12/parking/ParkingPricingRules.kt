package com.interview.case12.parking

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime

enum class VehicleType {
    CAR,
    MOTORCYCLE,
    EV
}

data class Money(val amount: BigDecimal) {
    init { require(amount.signum() >= 0) { "money cannot be negative" } }
}

data class ParkingSession(
    val vehicleType: VehicleType,
    val startsAt: LocalDateTime,
    val endsAt: LocalDateTime
)

data class ParkingTariff(
    val hourlyRate: Money,
    val nightMax: Money
)

class ParkingPriceCalculator {
    fun calculate(session: ParkingSession, tariff: ParkingTariff): Money {
        require(session.endsAt.isAfter(session.startsAt)) { "parking end must be after start" }

        val durationSeconds = Duration.between(session.startsAt, session.endsAt).seconds
        if (durationSeconds < FREE_SECONDS) return Money(BigDecimal.ZERO.setScale(2))

        val startedHours = (durationSeconds + SECONDS_PER_HOUR - 1) / SECONDS_PER_HOUR
        val ordinaryPrice = tariff.hourlyRate.amount
            .multiply(BigDecimal.valueOf(startedHours))
            .multiply(session.vehicleType.priceFactor)
        val cappedPrice = if (session.startsAt.toLocalTime().isNight()) {
            ordinaryPrice.min(tariff.nightMax.amount)
        } else {
            ordinaryPrice
        }
        return Money(cappedPrice.setScale(2, RoundingMode.HALF_UP))
    }

    private val VehicleType.priceFactor: BigDecimal
        get() = when (this) {
            VehicleType.CAR -> BigDecimal.ONE
            VehicleType.MOTORCYCLE -> BigDecimal("0.50")
            VehicleType.EV -> BigDecimal("0.75")
        }

    private fun LocalTime.isNight(): Boolean = this >= NIGHT_START || this < NIGHT_END

    private companion object {
        const val FREE_SECONDS = 15 * 60L
        const val SECONDS_PER_HOUR = 60 * 60L
        val NIGHT_START: LocalTime = LocalTime.of(22, 0)
        val NIGHT_END: LocalTime = LocalTime.of(6, 0)
    }
}
