package com.interview.case15.flight

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FlightSeatBookingTest {
    @Test
    fun `reserving a free seat should emit event`() {
        val flight = Flight()

        val event = flight.reserveSeat(SeatNumber("12A"), PassengerId("P-1"))

        assertThat(event.seatNumber.value).isEqualTo("12A")
    }
}
