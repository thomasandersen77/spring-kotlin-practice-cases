package com.interview.case23.concurrency

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class OptimisticLockingConcurrencyTest {

    @Test
    fun `reserve should decrease availability and increase version`() {
        val initial = InventoryReservation(
            id = ReservationId(UUID.randomUUID()),
            sku = "SKU-1",
            availableQuantity = 10,
            reservedQuantity = 0,
            version = Version(3)
        )

        val updated = initial.reserve(4)

        assertThat(updated.availableQuantity).isEqualTo(6)
        assertThat(updated.reservedQuantity).isEqualTo(4)
        assertThat(updated.version.value).isEqualTo(4)
    }

    @Test
    fun `exercise stale version should return conflict in use case`() {
        val initial = reservation()
        val repository = FakeRepository(initial, saveSucceeds = false)

        val result = ReserveInventoryUseCase(repository).reserve(ReserveInventoryCommand(initial.id, 1))

        assertThat(result).isEqualTo(ReserveResult.Conflict)
        assertThat(repository.expectedVersion).isEqualTo(initial.version)
    }

    @Test
    fun `accepted reservation should save next version`() {
        val initial = reservation()
        val repository = FakeRepository(initial)

        val result = ReserveInventoryUseCase(repository).reserve(ReserveInventoryCommand(initial.id, 2))

        assertThat(result).isInstanceOf(ReserveResult.Accepted::class.java)
        assertThat(repository.saved?.version).isEqualTo(Version(4))
    }

    @Test
    fun `invalid quantity unavailable stock and missing aggregate should be rejected`() {
        val initial = reservation()
        val useCase = ReserveInventoryUseCase(FakeRepository(initial))
        assertThat(useCase.reserve(ReserveInventoryCommand(initial.id, 0))).isEqualTo(ReserveResult.Rejected)
        assertThat(useCase.reserve(ReserveInventoryCommand(initial.id, 11))).isEqualTo(ReserveResult.Rejected)
        assertThat(ReserveInventoryUseCase(FakeRepository(null)).reserve(ReserveInventoryCommand(initial.id, 1)))
            .isEqualTo(ReserveResult.Rejected)
    }

    private fun reservation() = InventoryReservation(
        ReservationId(UUID.randomUUID()), "SKU-1", 10, 0, Version(3)
    )

    private class FakeRepository(
        private val current: InventoryReservation?,
        private val saveSucceeds: Boolean = true
    ) : InventoryReservationRepository {
        var expectedVersion: Version? = null
        var saved: InventoryReservation? = null
        override fun findById(id: ReservationId): InventoryReservation? = current
        override fun save(expectedVersion: Version, reservation: InventoryReservation): Boolean {
            this.expectedVersion = expectedVersion
            saved = reservation
            return saveSucceeds
        }
    }
}
