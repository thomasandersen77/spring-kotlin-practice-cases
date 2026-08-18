package com.training.case19.reservation

import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RestaurantReservationApiTest {
	@Test
	fun `valid reservation request should return accepted status`() {
		val useCase = CreateReservationUseCase()

		val response =
			useCase.execute(
				CreateReservationRequest(
					customerName = "Ada",
					partySize = 4,
					reservationTime = LocalDateTime.parse("2026-01-01T18:00:00"),
				)
			)

		assertThat(response.status).isEqualTo("ACCEPTED")
	}
}
