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

class ProcessSensorReadingUseCase(private val publisher: AlertPublisher) {
    fun process(reading: SensorReading) {
        TODO("Implement threshold strategy selection per sensor type and publish alerts only for rule violations")
    }
}
