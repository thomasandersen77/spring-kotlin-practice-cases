package com.interview.case19.reservation

import java.time.LocalDateTime
import java.util.UUID

data class CreateReservationRequest(val customerName: String, val partySize: Int, val reservationTime: LocalDateTime)
data class CreateReservationResponse(val reservationId: UUID, val status: String)

class CreateReservationUseCase {
    fun execute(request: CreateReservationRequest): CreateReservationResponse {
        TODO("Implement request validation and domain-aware reservation creation with explicit failure semantics")
    }
}
