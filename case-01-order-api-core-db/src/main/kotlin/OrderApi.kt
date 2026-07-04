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
 * Supertynn controller: mapper request DTO -> domain, delegerer til service,
 * returnerer response DTO.
 */
@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@Valid @RequestBody request: CreateOrderRequest): OrderResponse =
        orderService.createOrder(request.toDomain()).toResponse()

    @GetMapping("/{id}")
    fun getOrder(@PathVariable id: UUID): OrderResponse =
        orderService.getOrder(id).toResponse()

    @PostMapping("/{id}/confirm")
    fun confirmOrder(@PathVariable id: UUID): OrderResponse =
        orderService.confirmOrder(id).toResponse()

    @PostMapping("/{id}/cancel")
    fun cancelOrder(@PathVariable id: UUID): OrderResponse =
        orderService.cancelOrder(id).toResponse()
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
    val status: OrderStatus,
    val totalAmount: BigDecimal
)

fun Order.toResponse(): OrderResponse =
    OrderResponse(
        id = id,
        customerId = customerId,
        status = status,
        totalAmount = totalAmount()
    )
