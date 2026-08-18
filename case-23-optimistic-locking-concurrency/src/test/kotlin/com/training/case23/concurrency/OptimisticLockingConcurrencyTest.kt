package com.training.case23.concurrency

import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OptimisticLockingConcurrencyTest {

	@Test
	fun `reserve should decrease availability and increase version`() {
		val initial =
			InventoryReservation(
				id = ReservationId(UUID.randomUUID()),
				sku = "SKU-1",
				availableQuantity = 10,
				reservedQuantity = 0,
				version = Version(3),
			)

		val updated = initial.reserve(4)

		assertThat(updated.availableQuantity).isEqualTo(6)
		assertThat(updated.reservedQuantity).isEqualTo(4)
		assertThat(updated.version.value).isEqualTo(4)
	}

	@Test
	fun `exercise stale version should return conflict in use case`() {
		// Lag en testdouble for InventoryReservationRepository som returnerer false ved
		// save(expectedVersion, ...),
		// og verifiser at use case returnerer ReserveResult.Conflict.
	}
}
