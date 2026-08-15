import java.math.BigDecimal
import java.util.UUID

/**
 * DOMAIN / CORE
 *
 * Dette er domenemodellen. Den skal ikke vite noe om HTTP, JSON, JPA, Spring eller databasen.
 */
enum class OrderStatus {
 PENDING, CONFIRMED, CANCELLED
}

data class Order(
 val id: UUID,
 val customerId: UUID,
 val lines: List<OrderLine>,
 val status: OrderStatus = OrderStatus.PENDING
) {
 init {
 require(lines.isNotEmpty()) { "Order must have at least one order line" }
 }

 fun totalAmount(): BigDecimal =
 lines.fold(BigDecimal.ZERO) { acc, line -> acc + line.lineTotal() }

 fun cancel(): Order {
 require(status != OrderStatus.CANCELLED) { "Order is already cancelled" }
 return copy(status = OrderStatus.CANCELLED)
 }

 fun confirm(): Order {
 require(status == OrderStatus.PENDING) { "Only PENDING orders can be confirmed" }
 return copy(status = OrderStatus.CONFIRMED)
 }
}

data class OrderLine(
 val productId: UUID,
 val quantity: Int,
 val unitPrice: BigDecimal
) {
 fun lineTotal(): BigDecimal = unitPrice.multiply(BigDecimal(quantity))
}
