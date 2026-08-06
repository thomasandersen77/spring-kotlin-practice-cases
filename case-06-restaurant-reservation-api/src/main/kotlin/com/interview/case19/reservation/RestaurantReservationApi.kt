package com.interview.case19.reservation

import java.time.Clock
import java.time.LocalTime
import java.time.LocalDateTime
import java.util.UUID

data class CreateReservationRequest(val customerName: String, val partySize: Int, val reservationTime: LocalDateTime)
data class CreateReservationResponse(val reservationId: UUID, val status: String)

enum class ReservationStatus { ACCEPTED }

class CreateReservationUseCase(
    private val clock: Clock = Clock.systemDefaultZone(),
    private val idGenerator: () -> UUID = UUID::randomUUID
) {
    fun execute(request: CreateReservationRequest): CreateReservationResponse {
        require(request.customerName.isNotBlank()) { "customer name cannot be blank" }
        require(request.partySize in MIN_PARTY_SIZE..MAX_PARTY_SIZE) {
            "party size must be between $MIN_PARTY_SIZE and $MAX_PARTY_SIZE"
        }
        require(request.reservationTime.isAfter(LocalDateTime.now(clock))) {
            "reservation must be in the future"
        }
        require(request.reservationTime.toLocalTime() in OPENING_TIME..LAST_RESERVATION_TIME) {
            "reservation must be within opening hours"
        }
        return CreateReservationResponse(idGenerator(), ReservationStatus.ACCEPTED.name)
    }

    private companion object {
        const val MIN_PARTY_SIZE = 1
        const val MAX_PARTY_SIZE = 12
        val OPENING_TIME: LocalTime = LocalTime.of(17, 0)
        val LAST_RESERVATION_TIME: LocalTime = LocalTime.of(21, 30)
    }
}
