package com.interview.case12.parking

import java.math.BigDecimal
import java.time.LocalDateTime

enum class VehicleType {
    CAR,
    MOTORCYCLE,
    EV
}

data class Money(val amount: BigDecimal)

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
        TODO("Implement pricing, discounts, and rounding rules")
    }
}
