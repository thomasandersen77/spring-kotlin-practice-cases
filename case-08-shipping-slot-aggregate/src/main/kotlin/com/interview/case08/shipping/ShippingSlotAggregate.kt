package com.interview.case08.shipping

import java.time.LocalDateTime

data class TimeWindow(val start: LocalDateTime, val end: LocalDateTime) {
    init {
        require(start.isBefore(end)) { "start must be before end" }
    }
}

data class DeliverySlot(val slotId: String, val window: TimeWindow, val capacity: Int)
data class PackageBooking(val packageId: String, val slotId: String, val bookedAt: LocalDateTime)

class DeliveryRoute {
    fun book(slot: DeliverySlot, packageId: String, existing: List<PackageBooking>): PackageBooking {
        TODO("Implement booking invariants: slot capacity, duplicate package handling, and invalid input protection")
    }

    fun sortedByTime(bookings: List<PackageBooking>): List<PackageBooking> {
        TODO("Implement deterministic sorting that uses slot window start time instead of booking insertion order")
    }
}
