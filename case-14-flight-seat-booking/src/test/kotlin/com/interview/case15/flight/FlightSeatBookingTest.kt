package com.interview.case15.flight

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class FlightSeatBookingTest {
    @Test
    fun `reserving a free seat should emit event`() {
        val flight = Flight()

        val event = flight.reserveSeat(SeatNumber("12A"), PassengerId("P-1"))

        assertThat(event.seatNumber.value).isEqualTo("12A")
        assertThat(flight.reservations).containsExactly(
            SeatReservation(SeatNumber("12A"), PassengerId("P-1"))
        )
    }

    @Test
    fun `seat and passenger cannot be reserved twice`() {
        val flight = Flight()
        flight.reserveSeat(SeatNumber("12A"), PassengerId("P-1"))

        assertThatThrownBy { flight.reserveSeat(SeatNumber("12A"), PassengerId("P-2")) }
            .isInstanceOf(IllegalStateException::class.java).hasMessageContaining("seat")
        assertThatThrownBy { flight.reserveSeat(SeatNumber("12B"), PassengerId("P-1")) }
            .isInstanceOf(IllegalStateException::class.java).hasMessageContaining("passenger")
    }

    @Test
    fun `cancelled seat can be rebooked`() {
        val flight = Flight()
        flight.reserveSeat(SeatNumber("12A"), PassengerId("P-1"))

        flight.cancelReservation(SeatNumber("12A"))
        val event = flight.reserveSeat(SeatNumber("12A"), PassengerId("P-2"))

        assertThat(event.passengerId).isEqualTo(PassengerId("P-2"))
        assertThat(flight.reservations).containsExactly(
            SeatReservation(SeatNumber("12A"), PassengerId("P-2"))
        )
    }

    @Test
    fun `cancelling unknown seat should fail`() {
        assertThatThrownBy { Flight().cancelReservation(SeatNumber("12A")) }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
