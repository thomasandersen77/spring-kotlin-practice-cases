package com.interview.case15.flight

@JvmInline
value class SeatNumber(val value: String) {
    init { require(value.isNotBlank()) { "seat number cannot be blank" } }
}

@JvmInline
value class PassengerId(val value: String) {
    init { require(value.isNotBlank()) { "passenger id cannot be blank" } }
}

data class SeatReservation(val seatNumber: SeatNumber, val passengerId: PassengerId)
data class SeatReserved(val seatNumber: SeatNumber, val passengerId: PassengerId)

class Flight {
    private val reservationsBySeat = linkedMapOf<SeatNumber, SeatReservation>()
    val reservations: List<SeatReservation> get() = reservationsBySeat.values.toList()

    fun reserveSeat(seatNumber: SeatNumber, passengerId: PassengerId): SeatReserved {
        check(seatNumber !in reservationsBySeat) { "seat is already reserved" }
        check(reservationsBySeat.values.none { it.passengerId == passengerId }) {
            "passenger already has a seat"
        }
        reservationsBySeat[seatNumber] = SeatReservation(seatNumber, passengerId)
        return SeatReserved(seatNumber, passengerId)
    }

    fun cancelReservation(seatNumber: SeatNumber) {
        checkNotNull(reservationsBySeat.remove(seatNumber)) { "seat has no reservation" }
    }
}
