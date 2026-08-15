import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * APPLICATION SERVICE / USE CASE
 *
 * Dette laget orkestrerer use case-et.
 * Transaksjonsgrenser settes her slik at LAZY-relasjoner er tilgjengelige under mapping.
 */
@Service
class OrderService(
 private val orderRepository: OrderRepository
) {
 @Transactional
 fun createOrder(order: Order): Order {
 require(order.lines.isNotEmpty()) { "Order must have at least one order line" }
 return orderRepository.save(order)
 }

 @Transactional(readOnly = true)
 fun getOrder(id: UUID): Order =
 orderRepository.findById(id) ?: throw NoSuchElementException("Order $id not found")

 @Transactional
 fun cancelOrder(id: UUID): Order {
 val order = getOrder(id)
 return orderRepository.save(order.cancel())
 }

 @Transactional
 fun confirmOrder(id: UUID): Order {
 val order = getOrder(id)
 return orderRepository.save(order.confirm())
 }
}

/**
 * Repository-port.
 *
 * Core/service avhenger av dette grensesnittet, ikke Spring Data direkte.
 */
interface OrderRepository {
 fun save(order: Order): Order
 fun findById(id: UUID): Order?
}
