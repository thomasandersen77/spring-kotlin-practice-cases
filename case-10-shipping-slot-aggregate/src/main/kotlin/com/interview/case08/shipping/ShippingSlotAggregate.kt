package com.interview.case08.shipping

import java.time.Clock
import java.time.LocalDateTime

data class TimeWindow(val start: LocalDateTime, val end: LocalDateTime) {
    init {
        require(start.isBefore(end)) { "start must be before end" }
    }
}

data class DeliverySlot(val slotId: String, val window: TimeWindow, val capacity: Int) {
    init {
        require(slotId.isNotBlank()) { "slot id cannot be blank" }
        require(capacity > 0) { "slot capacity must be positive" }
    }
}
data class PackageBooking(val packageId: String, val slotId: String, val bookedAt: LocalDateTime)

class DeliveryRoute(private val clock: Clock = Clock.systemUTC()) {
    private val bookingsByPackage = linkedMapOf<String, PackageBooking>()
    private val windowsBySlot = mutableMapOf<String, TimeWindow>()

    val bookings: List<PackageBooking> get() = bookingsByPackage.values.toList()

    fun book(slot: DeliverySlot, packageId: String, existing: List<PackageBooking>): PackageBooking {
        require(packageId.isNotBlank()) { "package id cannot be blank" }
        existing.forEach { booking ->
            require(booking.packageId.isNotBlank() && booking.slotId.isNotBlank()) { "existing booking is invalid" }
            bookingsByPackage.putIfAbsent(booking.packageId, booking)
        }
        check(packageId !in bookingsByPackage) { "package is already booked" }
        check(bookingsByPackage.values.count { it.slotId == slot.slotId } < slot.capacity) { "slot is full" }

        windowsBySlot[slot.slotId]?.let {
            require(it == slot.window) { "slot id cannot refer to different time windows" }
        }
        windowsBySlot[slot.slotId] = slot.window

        return PackageBooking(packageId, slot.slotId, LocalDateTime.now(clock))
            .also { bookingsByPackage[packageId] = it }
    }

    fun sortedByTime(bookings: List<PackageBooking>): List<PackageBooking> {
        require(bookings.all { it.slotId in windowsBySlot }) { "all booking slots must be known to the route" }
        return bookings.sortedWith(compareBy({ windowsBySlot.getValue(it.slotId).start }, { it.slotId }, { it.packageId }))
    }

    fun sortedByTime(bookings: List<PackageBooking>, slots: Collection<DeliverySlot>): List<PackageBooking> {
        slots.forEach { windowsBySlot[it.slotId] = it.window }
        return sortedByTime(bookings)
    }
}
