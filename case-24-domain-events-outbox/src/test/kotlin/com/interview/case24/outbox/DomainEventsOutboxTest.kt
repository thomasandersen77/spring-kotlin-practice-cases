package com.interview.case24.outbox

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class DomainEventsOutboxTest {

    @Test
    fun `outbox message should map domain event metadata`() {
        val event = OrderPlaced(
            eventId = EventId(UUID.randomUUID()),
            orderId = OrderId(UUID.randomUUID()),
            occurredAt = Instant.parse("2026-01-01T10:15:30Z"),
            customerId = "cust-42",
            amount = BigDecimal("199.00")
        )

        val message = OutboxMessage.from(event)

        assertThat(message.eventType).isEqualTo("OrderPlaced")
        assertThat(message.messageId).isEqualTo(event.eventId.value)
        assertThat(message.occurredAt).isEqualTo(event.occurredAt)
        assertThat(message.payload).contains("cust-42")
    }

    @Test
    fun `exercise use case should treat order save and outbox append as one transaction boundary`() {
        val orders = mutableListOf<PurchaseOrder>()
        val messages = mutableListOf<OutboxMessage>()
        var transactions = 0
        val useCase = PlaceOrderUseCase(
            { orders += it },
            { messages += it },
            TransactionBoundary { block -> transactions++; block() }
        )

        val id = useCase.place(PlaceOrderCommand("cust-42", BigDecimal("199.00")), Instant.parse("2026-01-01T10:15:30Z"))

        assertThat(transactions).isEqualTo(1)
        assertThat(orders.single().id).isEqualTo(id)
        assertThat(messages.single().payload).contains(id.value.toString())
    }

    @Test
    fun `outbox failure should propagate from transaction boundary`() {
        val useCase = PlaceOrderUseCase(
            { },
            { throw IllegalStateException("outbox unavailable") },
            TransactionBoundary { block -> block() }
        )

        assertThatThrownBy { useCase.place(PlaceOrderCommand("cust", BigDecimal.TEN)) }
            .isInstanceOf(IllegalStateException::class.java).hasMessageContaining("outbox")
    }

    @Test
    fun `invalid order command should fail before persistence`() {
        var writes = 0
        val useCase = PlaceOrderUseCase({ writes++ }, { writes++ })
        assertThatThrownBy { useCase.place(PlaceOrderCommand("bad customer!", BigDecimal.ONE)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { useCase.place(PlaceOrderCommand("cust", BigDecimal.ZERO)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThat(writes).isZero()
    }
}
