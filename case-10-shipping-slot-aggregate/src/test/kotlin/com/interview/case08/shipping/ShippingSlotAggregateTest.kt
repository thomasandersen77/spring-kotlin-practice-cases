package com.interview.case08.shipping

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

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
        val slot = DeliverySlot(
            slotId = "SLOT-1",
            window = TimeWindow(
                LocalDateTime.parse("2026-01-01T10:00:00"),
                LocalDateTime.parse("2026-01-01T11:00:00")
            ),
            capacity = 2
        )

        val booking = route.book(slot, "PKG-1", emptyList())

        assertThat(booking.packageId).isEqualTo("PKG-1")
        assertThat(route.bookings).containsExactly(booking)
    }

    @Test
    fun `full slot and duplicate package should be rejected`() {
        val route = DeliveryRoute()
        val first = slot("SLOT-1", "2026-01-01T10:00:00", capacity = 1)
        val second = slot("SLOT-2", "2026-01-01T11:00:00", capacity = 2)
        val existing = route.book(first, "PKG-1", emptyList())

        assertThatThrownBy { route.book(first, "PKG-2", listOf(existing)) }
            .isInstanceOf(IllegalStateException::class.java).hasMessageContaining("full")
        assertThatThrownBy { route.book(second, "PKG-1", listOf(existing)) }
            .isInstanceOf(IllegalStateException::class.java).hasMessageContaining("already")
    }

    @Test
    fun `bookings should sort by slot start and deterministic tie breakers`() {
        val route = DeliveryRoute()
        val late = slot("B", "2026-01-01T12:00:00")
        val early = slot("A", "2026-01-01T09:00:00")
        val lateBooking = route.book(late, "PKG-2", emptyList())
        val earlyBooking = route.book(early, "PKG-1", emptyList())

        assertThat(route.sortedByTime(listOf(lateBooking, earlyBooking)))
            .containsExactly(earlyBooking, lateBooking)
    }

    @Test
    fun `slot should require positive capacity`() {
        assertThatThrownBy { slot("SLOT", "2026-01-01T10:00:00", 0) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    private fun slot(id: String, start: String, capacity: Int = 2): DeliverySlot {
        val from = LocalDateTime.parse(start)
        return DeliverySlot(id, TimeWindow(from, from.plusHours(1)), capacity)
    }
}
