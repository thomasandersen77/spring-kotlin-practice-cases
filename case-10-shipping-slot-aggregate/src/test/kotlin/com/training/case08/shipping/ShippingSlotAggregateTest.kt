package com.training.case08.shipping

import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ShippingSlotAggregateTest {
	@Test
	fun `time window should require start before end`() {
		val start = LocalDateTime.parse("2026-01-01T10:00:00")
		val end = LocalDateTime.parse("2026-01-01T11:00:00")

		val window = TimeWindow(start, end)

		assertThat(window.start).isEqualTo(start)
	}

	@Test
	fun `should book package in available slot`() {
		val route = DeliveryRoute()
		val slot =
			DeliverySlot(
				slotId = "SLOT-1",
				window =
					TimeWindow(
						LocalDateTime.parse("2026-01-01T10:00:00"),
						LocalDateTime.parse("2026-01-01T11:00:00"),
					),
				capacity = 2,
			)

		val booking = route.book(slot, "PKG-1", emptyList())

		assertThat(booking.packageId).isEqualTo("PKG-1")
	}
}
