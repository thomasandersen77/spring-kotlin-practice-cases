package com.training.case22.sensor

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IotSensorAlertingTest {
	@Test
	fun `temperature above threshold should publish alert`() {
		val publishedAlerts = mutableListOf<Alert>()
		val publisher =
			object : AlertPublisher {
				override fun publish(alert: Alert) {
					publishedAlerts += alert
				}
			}
		val useCase = ProcessSensorReadingUseCase(publisher)

		useCase.process(
			SensorReading(sensorId = "S-1", type = SensorType.TEMPERATURE, value = 90.0)
		)

		assertThat(publishedAlerts).hasSize(1)
	}
}
