package com.interview.case23.concurrency

import java.util.UUID

@JvmInline
value class ReservationId(val value: UUID)

@JvmInline
value class Version(val value: Long)

data class InventoryReservation(
    val id: ReservationId,
    val sku: String,
    val availableQuantity: Int,
    val reservedQuantity: Int,
    val version: Version
) {
    init {
        require(sku.isNotBlank()) { "sku cannot be blank" }
        require(version.value >= 0) { "version must be >= 0" }
        require(availableQuantity >= 0) { "availableQuantity must be >= 0" }
        require(reservedQuantity >= 0) { "reservedQuantity must be >= 0" }
    }

    fun reserve(quantity: Int): InventoryReservation {
        require(quantity > 0) { "quantity must be positive" }
        require(quantity <= availableQuantity) { "cannot reserve more than available quantity" }

        return copy(
            availableQuantity = availableQuantity - quantity,
            reservedQuantity = reservedQuantity + quantity,
            version = Version(version.value + 1)
        )
    }
}

data class ReserveInventoryCommand(val reservationId: ReservationId, val quantity: Int)

sealed class ReserveResult {
    data class Accepted(val reservation: InventoryReservation) : ReserveResult()
    data object Conflict : ReserveResult()
    data object Rejected : ReserveResult()
}

interface InventoryReservationRepository {
    fun findById(id: ReservationId): InventoryReservation?
    fun save(expectedVersion: Version, reservation: InventoryReservation): Boolean
}

class ReserveInventoryUseCase(private val repository: InventoryReservationRepository) {
    fun reserve(command: ReserveInventoryCommand): ReserveResult {
        val current = repository.findById(command.reservationId) ?: return ReserveResult.Rejected
        val updated = try {
            current.reserve(command.quantity)
        } catch (_: IllegalArgumentException) {
            return ReserveResult.Rejected
        }
        return if (repository.save(current.version, updated)) {
            ReserveResult.Accepted(updated)
        } else {
            ReserveResult.Conflict
        }
    }
}
