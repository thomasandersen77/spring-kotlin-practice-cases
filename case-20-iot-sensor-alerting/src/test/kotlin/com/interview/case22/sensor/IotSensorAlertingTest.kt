package com.interview.case22.sensor

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class IotSensorAlertingTest {
    @Test
    fun `temperature above threshold should publish alert`() {
        val publishedAlerts = mutableListOf<Alert>()
        val publisher = object : AlertPublisher {
            override fun publish(alert: Alert) {
                publishedAlerts += alert
            }
        }
        val useCase = ProcessSensorReadingUseCase(publisher)

        useCase.process(SensorReading(sensorId = "S-1", type = SensorType.TEMPERATURE, value = 90.0))

        assertThat(publishedAlerts).hasSize(1)
        assertThat(publishedAlerts.single().message).contains("TEMPERATURE", "80.0")
    }

    @Test
    fun `normal readings should not publish alerts`() {
        val alerts = mutableListOf<Alert>()
        val useCase = ProcessSensorReadingUseCase(object : AlertPublisher {
            override fun publish(alert: Alert) { alerts += alert }
        })

        useCase.process(SensorReading("T", SensorType.TEMPERATURE, 80.0))
        useCase.process(SensorReading("H", SensorType.HUMIDITY, 50.0))
        useCase.process(SensorReading("V", SensorType.VIBRATION, 2.0))

        assertThat(alerts).isEmpty()
    }

    @Test
    fun `humidity and vibration thresholds should publish`() {
        val alerts = mutableListOf<Alert>()
        val useCase = ProcessSensorReadingUseCase(object : AlertPublisher {
            override fun publish(alert: Alert) { alerts += alert }
        })

        useCase.process(SensorReading("H", SensorType.HUMIDITY, 91.0))
        useCase.process(SensorReading("V", SensorType.VIBRATION, 10.1))

        assertThat(alerts.map(Alert::sensorId)).containsExactly("H", "V")
    }

    @Test
    fun `invalid sensor values and ids should be rejected`() {
        val useCase = ProcessSensorReadingUseCase(object : AlertPublisher { override fun publish(alert: Alert) = Unit })
        assertThatThrownBy { useCase.process(SensorReading("", SensorType.TEMPERATURE, 1.0)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { useCase.process(SensorReading("H", SensorType.HUMIDITY, -1.0)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { useCase.process(SensorReading("T", SensorType.TEMPERATURE, Double.NaN)) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
