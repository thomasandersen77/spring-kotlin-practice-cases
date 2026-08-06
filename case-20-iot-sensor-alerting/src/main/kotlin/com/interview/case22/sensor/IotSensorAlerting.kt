package com.interview.case22.sensor

enum class SensorType {
    TEMPERATURE,
    HUMIDITY,
    VIBRATION
}

data class SensorReading(val sensorId: String, val type: SensorType, val value: Double)
data class Alert(val sensorId: String, val message: String)

interface AlertPublisher {
    fun publish(alert: Alert)
}

interface SensorAlertRule {
    val type: SensorType
    fun violation(value: Double): String?
}

class MaximumThresholdRule(
    override val type: SensorType,
    private val maximum: Double,
    private val unit: String,
    private val allowNegative: Boolean = true
) : SensorAlertRule {
    override fun violation(value: Double): String? {
        require(value.isFinite()) { "sensor value must be finite" }
        require(allowNegative || value >= 0) { "$type value cannot be negative" }
        return if (value > maximum) "$type value $value $unit exceeds maximum $maximum $unit" else null
    }
}

class ProcessSensorReadingUseCase(
    private val publisher: AlertPublisher,
    rules: List<SensorAlertRule> = defaultRules()
) {
    private val rulesByType = rules.associateBy(SensorAlertRule::type)

    fun process(reading: SensorReading) {
        require(reading.sensorId.isNotBlank()) { "sensor id cannot be blank" }
        val rule = rulesByType[reading.type] ?: error("No alert rule for ${reading.type}")
        rule.violation(reading.value)?.let { publisher.publish(Alert(reading.sensorId, it)) }
    }

    private companion object {
        fun defaultRules(): List<SensorAlertRule> = listOf(
            MaximumThresholdRule(SensorType.TEMPERATURE, 80.0, "°C"),
            MaximumThresholdRule(SensorType.HUMIDITY, 90.0, "%", allowNegative = false),
            MaximumThresholdRule(SensorType.VIBRATION, 10.0, "mm/s", allowNegative = false)
        )
    }
}
