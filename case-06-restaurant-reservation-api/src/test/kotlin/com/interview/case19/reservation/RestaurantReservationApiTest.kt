package com.interview.case19.reservation

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

class RestaurantReservationApiTest {
    @Test
    fun `valid reservation request should return accepted status`() {
        val id = UUID.fromString("00000000-0000-0000-0000-000000000001")
        val useCase = CreateReservationUseCase(fixedClock(), idGenerator = { id })

        val response = useCase.execute(
            CreateReservationRequest(
                customerName = "Ada",
                partySize = 4,
                reservationTime = LocalDateTime.parse("2026-01-01T18:00:00")
            )
        )

        assertThat(response.status).isEqualTo("ACCEPTED")
        assertThat(response.reservationId).isEqualTo(id)
    }

    @Test
    fun `blank customer and invalid party size should be rejected`() {
        val useCase = CreateReservationUseCase(fixedClock())
        assertThatThrownBy { useCase.execute(request(name = " ")) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { useCase.execute(request(partySize = 13)) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `past reservation and time outside opening hours should be rejected`() {
        val useCase = CreateReservationUseCase(fixedClock())
        assertThatThrownBy {
            useCase.execute(request(time = LocalDateTime.parse("2025-12-31T18:00:00")))
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessageContaining("future")
        assertThatThrownBy {
            useCase.execute(request(time = LocalDateTime.parse("2026-01-02T16:59:00")))
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessageContaining("opening")
    }

    private fun request(
        name: String = "Ada",
        partySize: Int = 4,
        time: LocalDateTime = LocalDateTime.parse("2026-01-02T18:00:00")
    ) = CreateReservationRequest(name, partySize, time)

    private fun fixedClock(): Clock = Clock.fixed(Instant.parse("2026-01-01T12:00:00Z"), ZoneOffset.UTC)
}
