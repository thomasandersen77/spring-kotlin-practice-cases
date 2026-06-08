import java.math.BigDecimal
import java.util.UUID

/**
 * DOMAIN / CORE
 *
 * Dette er domenemodellen. Den skal helst ikke vite noe om HTTP, JSON, JPA eller databasen.
 *
 * TODO:
 *  - Legg til validering av at order lines ikke er tomme
 *  - Legg til domeneoperasjon for å kansellere ordre
 *  - Legg til statusfelt: DRAFT, CONFIRMED, CANCELLED
 *  - Sørg for at total beregnes i domenet, ikke i controller
 */
data class Order(
    val id: UUID,
    val customerId: UUID,
    val lines: List<OrderLine>
) {
    fun totalAmount(): BigDecimal =
        lines.fold(BigDecimal.ZERO) { acc, line -> acc + line.lineTotal() }
}

data class OrderLine(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: BigDecimal
) {
    fun lineTotal(): BigDecimal = unitPrice.multiply(BigDecimal(quantity))
}
