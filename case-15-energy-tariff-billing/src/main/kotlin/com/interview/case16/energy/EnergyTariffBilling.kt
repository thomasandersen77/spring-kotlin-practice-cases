package com.interview.case16.energy

import java.math.BigDecimal
import java.math.RoundingMode

data class MeterReading(val value: BigDecimal) {
    init { require(value.signum() >= 0) { "meter reading cannot be negative" } }
}
data class EnergyTariff(val spotPerKwh: BigDecimal, val gridPerKwh: BigDecimal, val fixedMonthly: BigDecimal) {
    init {
        require(listOf(spotPerKwh, gridPerKwh, fixedMonthly).all { it.signum() >= 0 }) {
            "tariff components cannot be negative"
        }
    }
}
data class EnergyBill(
    val total: BigDecimal,
    val consumptionKwh: BigDecimal = BigDecimal.ZERO,
    val variableCost: BigDecimal = BigDecimal.ZERO,
    val fixedCost: BigDecimal = BigDecimal.ZERO,
    val vat: BigDecimal = BigDecimal.ZERO
)

class EnergyBillCalculator {
    fun calculate(start: MeterReading, end: MeterReading, tariff: EnergyTariff, vatRate: BigDecimal): EnergyBill {
        require(end.value >= start.value) { "end reading cannot be lower than start reading" }
        require(vatRate >= BigDecimal.ZERO && vatRate <= BigDecimal.ONE) { "vat rate must be between 0 and 1" }

        val consumption = end.value.subtract(start.value)
        val variableCost = consumption.multiply(tariff.spotPerKwh.add(tariff.gridPerKwh))
        val beforeVat = variableCost.add(tariff.fixedMonthly)
        val vat = beforeVat.multiply(vatRate)
        val total = beforeVat.add(vat).setScale(2, RoundingMode.HALF_UP)
        return EnergyBill(total, consumption, variableCost, tariff.fixedMonthly, vat)
    }
}
