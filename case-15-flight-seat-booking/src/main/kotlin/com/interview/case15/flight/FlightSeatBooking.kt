package com.interview.case15.flight

@JvmInline
value class SeatNumber(val value: String)

@JvmInline
value class PassengerId(val value: String)

data class SeatReservation(val seatNumber: SeatNumber, val passengerId: PassengerId)
data class SeatReserved(val seatNumber: SeatNumber, val passengerId: PassengerId)

class Flight {
    fun reserveSeat(seatNumber: SeatNumber, passengerId: PassengerId): SeatReserved {
        TODO("Implement seat reservation invariants")
    }

    fun cancelReservation(seatNumber: SeatNumber) {
        TODO("Implement cancellation")
    }
}
