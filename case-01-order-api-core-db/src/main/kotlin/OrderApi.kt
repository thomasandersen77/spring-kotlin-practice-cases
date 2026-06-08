import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID

/**
 * REST CONTROLLER
 *
 * Målet er supertynn controller.
 *
 * TODO:
 *  - Ikke legg forretningslogikk her
 *  - Map request DTO -> command/domain
 *  - Returner response DTO, ikke JPA-entity
 *  - Legg til GET /orders/{id}
 *  - Legg til POST /orders/{id}/confirm
 */
@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@Valid @RequestBody request: CreateOrderRequest): OrderResponse {
        val order = orderService.createOrder(request.toDomain())
        return order.toResponse()
    }
}

data class CreateOrderRequest(
    @field:NotNull
    val customerId: UUID?,

    @field:NotEmpty
    val lines: List<CreateOrderLineRequest>
) {
    fun toDomain(): Order =
        Order(
            id = UUID.randomUUID(),
            customerId = requireNotNull(customerId),
            lines = lines.map { it.toDomain() }
        )
}

data class CreateOrderLineRequest(
    @field:NotNull
    val productId: UUID?,

    @field:Min(1)
    val quantity: Int,

    @field:NotNull
    val unitPrice: BigDecimal?
) {
    fun toDomain(): OrderLine =
        OrderLine(
            productId = requireNotNull(productId),
            quantity = quantity,
            unitPrice = requireNotNull(unitPrice)
        )
}

data class OrderResponse(
    val id: UUID,
    val customerId: UUID,
    val totalAmount: BigDecimal
)

fun Order.toResponse(): OrderResponse =
    OrderResponse(
        id = id,
        customerId = customerId,
        totalAmount = totalAmount()
    )
