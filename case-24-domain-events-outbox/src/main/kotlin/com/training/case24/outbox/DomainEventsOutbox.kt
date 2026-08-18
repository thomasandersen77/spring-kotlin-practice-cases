package com.training.case24.outbox

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@JvmInline value class OrderId(val value: UUID)

@JvmInline value class EventId(val value: UUID)

data class PlaceOrderCommand(val customerId: String, val amount: BigDecimal)

data class PurchaseOrder(
	val id: OrderId,
	val customerId: String,
	val amount: BigDecimal,
	val placedAt: Instant,
)

data class OrderPlaced(
	val eventId: EventId,
	val orderId: OrderId,
	val occurredAt: Instant,
	val customerId: String,
	val amount: BigDecimal,
)

data class OutboxMessage(
	val messageId: UUID,
	val eventType: String,
	val payload: String,
	val occurredAt: Instant,
) {
	companion object {
		fun from(event: OrderPlaced): OutboxMessage {
			return OutboxMessage(
				messageId = event.eventId.value,
				eventType = "OrderPlaced",
				payload =
					"""{"orderId":"${event.orderId.value}","customerId":"${event.customerId}","amount":"${event.amount}"}""",
				occurredAt = event.occurredAt,
			)
		}
	}
}

interface PurchaseOrderRepository {
	fun save(order: PurchaseOrder)
}

interface OutboxRepository {
	fun append(message: OutboxMessage)
}

class PlaceOrderUseCase(
	private val orderRepository: PurchaseOrderRepository,
	private val outboxRepository: OutboxRepository,
) {
	fun place(command: PlaceOrderCommand, now: Instant = Instant.now()): OrderId {
		TODO(
			"Implement application flow: validate command, create order, create event, persist order and outbox atomically"
		)
	}
}
