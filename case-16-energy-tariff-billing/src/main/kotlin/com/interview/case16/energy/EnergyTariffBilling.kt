package com.interview.case16.energy

import java.math.BigDecimal

data class MeterReading(val value: BigDecimal)
data class EnergyTariff(val spotPerKwh: BigDecimal, val gridPerKwh: BigDecimal, val fixedMonthly: BigDecimal)
data class EnergyBill(val total: BigDecimal)

class EnergyBillCalculator {
    fun calculate(start: MeterReading, end: MeterReading, tariff: EnergyTariff, vatRate: BigDecimal): EnergyBill {
        TODO("Implement kWh difference calculation, tariff composition, VAT application, and deterministic rounding")
    }
}
