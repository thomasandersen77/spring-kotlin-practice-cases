package com.training.case24.outbox

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DomainEventsOutboxTest {

	@Test
	fun `outbox message should map domain event metadata`() {
		val event =
			OrderPlaced(
				eventId = EventId(UUID.randomUUID()),
				orderId = OrderId(UUID.randomUUID()),
				occurredAt = Instant.parse("2026-01-01T10:15:30Z"),
				customerId = "cust-42",
				amount = BigDecimal("199.00"),
			)

		val message = OutboxMessage.from(event)

		assertThat(message.eventType).isEqualTo("OrderPlaced")
		assertThat(message.messageId).isEqualTo(event.eventId.value)
		assertThat(message.occurredAt).isEqualTo(event.occurredAt)
		assertThat(message.payload).contains("cust-42")
	}

	@Test
	fun `exercise use case should treat order save and outbox append as one transaction boundary`() {
		// Bygg to testdoubles (orderRepository/outboxRepository) og beskriv i test
		// hva som er forventet atferd ved feil i outbox-skriving.
	}
}
