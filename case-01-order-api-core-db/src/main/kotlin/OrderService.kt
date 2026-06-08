import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * APPLICATION SERVICE / USE CASE
 *
 * Dette laget orkestrerer use case-et.
 *
 * TODO:
 *  - Legg inn transaksjonsgrenser bevisst
 *  - Ikke lek JPA-entities ut fra service
 *  - Vurder om service skal ta command-objekter i stedet for domain direkte
 *  - Legg til feilhåndtering ved manglende ordre
 */
@Service
class OrderService(
    private val orderRepository: OrderRepository
) {
    @Transactional
    fun createOrder(order: Order): Order {
        // TODO: Legg til domeneregler før lagring.
        return orderRepository.save(order)
    }

    @Transactional(readOnly = true)
    fun getOrder(id: UUID): Order {
        TODO("Implementer henting av ordre")
    }
}

/**
 * Repository-port.
 *
 * Core/service bør avhenge av dette grensesnittet, ikke Spring Data direkte.
 */
interface OrderRepository {
    fun save(order: Order): Order
    fun findById(id: UUID): Order?
}
