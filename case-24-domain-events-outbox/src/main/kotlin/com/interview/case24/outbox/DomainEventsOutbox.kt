package com.interview.case24.outbox

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@JvmInline
value class OrderId(val value: UUID)

@JvmInline
value class EventId(val value: UUID)

data class PlaceOrderCommand(val customerId: String, val amount: BigDecimal)

data class PurchaseOrder(val id: OrderId, val customerId: String, val amount: BigDecimal, val placedAt: Instant)

data class OrderPlaced(
    val eventId: EventId,
    val orderId: OrderId,
    val occurredAt: Instant,
    val customerId: String,
    val amount: BigDecimal
)

data class OutboxMessage(
    val messageId: UUID,
    val eventType: String,
    val payload: String,
    val occurredAt: Instant
) {
    companion object {
        fun from(event: OrderPlaced): OutboxMessage {
            return OutboxMessage(
                messageId = event.eventId.value,
                eventType = "OrderPlaced",
                payload = """{"orderId":"${event.orderId.value}","customerId":"${event.customerId}","amount":"${event.amount}"}""",
                occurredAt = event.occurredAt
            )
        }
    }
}

fun interface PurchaseOrderRepository {
    fun save(order: PurchaseOrder)
}

fun interface OutboxRepository {
    fun append(message: OutboxMessage)
}

fun interface TransactionBoundary {
    fun inTransaction(block: () -> OrderId): OrderId
}

class PlaceOrderUseCase(
    private val orderRepository: PurchaseOrderRepository,
    private val outboxRepository: OutboxRepository,
    private val transactionBoundary: TransactionBoundary = TransactionBoundary { it() }
) {
    fun place(command: PlaceOrderCommand, now: Instant = Instant.now()): OrderId {
        require(command.customerId.matches(Regex("[A-Za-z0-9_-]+"))) { "customer id is invalid" }
        require(command.amount.signum() > 0) { "amount must be positive" }

        val orderId = OrderId(UUID.randomUUID())
        val order = PurchaseOrder(orderId, command.customerId, command.amount, now)
        val event = OrderPlaced(EventId(UUID.randomUUID()), orderId, now, command.customerId, command.amount)
        val message = OutboxMessage.from(event)

        return transactionBoundary.inTransaction {
            orderRepository.save(order)
            outboxRepository.append(message)
            orderId
        }
    }
}
