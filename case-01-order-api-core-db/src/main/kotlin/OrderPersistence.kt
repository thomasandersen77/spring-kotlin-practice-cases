import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.UUID

/**
 * PERSISTENCE / ADAPTER
 *
 * Her er JPA isolert.
 *
 * TODO:
 *  - Fullfør mapping mellom domain og entity
 *  - Vurder cascade og orphanRemoval
 *  - Vurder lazy loading
 *  - Vurder hvorfor mapping utenfor transaksjon kan feile med LAZY-relasjoner
 */
@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    var customerId: UUID = UUID.randomUUID(),

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var lines: MutableList<OrderLineEntity> = mutableListOf()
)

@Entity
@Table(name = "order_lines")
class OrderLineEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    var productId: UUID = UUID.randomUUID(),

    var quantity: Int = 0,

    var unitPrice: BigDecimal = BigDecimal.ZERO,

    @ManyToOne(fetch = FetchType.LAZY)
    var order: OrderEntity? = null
)

interface SpringDataOrderRepository : JpaRepository<OrderEntity, UUID>

@Repository
class JpaOrderRepository(
    private val springDataRepository: SpringDataOrderRepository
) : OrderRepository {

    override fun save(order: Order): Order {
        val entity = order.toEntity()
        return springDataRepository.save(entity).toDomain()
    }

    override fun findById(id: UUID): Order? =
        springDataRepository.findById(id).orElse(null)?.toDomain()
}

fun Order.toEntity(): OrderEntity {
    val orderEntity = OrderEntity(
        id = id,
        customerId = customerId
    )

    orderEntity.lines = lines.map {
        OrderLineEntity(
            productId = it.productId,
            quantity = it.quantity,
            unitPrice = it.unitPrice,
            order = orderEntity
        )
    }.toMutableList()

    return orderEntity
}

fun OrderEntity.toDomain(): Order =
    Order(
        id = id,
        customerId = customerId,
        lines = lines.map {
            OrderLine(
                productId = it.productId,
                quantity = it.quantity,
                unitPrice = it.unitPrice
            )
        }
    )
