import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

/**
 * PERSISTENCE / ADAPTER
 *
 * Her er JPA isolert.
 *
 * Cascade ALL + orphanRemoval = true er riktig for en aggregatrot:
 * alle linjer lever og dør med ordren.
 *
 * FetchType.LAZY brukes for ytelse. Mapping (toDomain) må skje innenfor
 * en aktiv transaksjon, ellers kastes LazyInitializationException.
 */
@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    var customerId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING,

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

fun Order.toEntity(): OrderEntity {
    val orderEntity = OrderEntity(
        id = id,
        customerId = customerId,
        status = status
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
        status = status,
        lines = lines.map {
            OrderLine(
                productId = it.productId,
                quantity = it.quantity,
                unitPrice = it.unitPrice
            )
        }
    )

interface OrderJpaRepository : JpaRepository<OrderEntity, UUID>

@Repository
class JpaOrderRepository(
    private val springDataRepository: OrderJpaRepository
) : OrderRepository {

    @Transactional
    override fun save(order: Order): Order =
        springDataRepository.save(order.toEntity()).toDomain()

    @Transactional(readOnly = true)
    override fun findById(id: UUID): Order? =
        springDataRepository.findById(id).orElse(null)?.toDomain()
}
