import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class OrderDomainTest {

    @Test
    fun `should calculate total amount`() {
        val order = Order(
            id = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            lines = listOf(
                OrderLine(UUID.randomUUID(), 2, BigDecimal("100.00")),
                OrderLine(UUID.randomUUID(), 1, BigDecimal("50.00"))
            )
        )

        assertThat(order.totalAmount()).isEqualByComparingTo("250.00")
    }

    @Test
    fun `exercise should reject empty order`() {
        // Legg til en eksplisitt domeneinvariant for tom ordre og dokumenter ønsket feilkontrakt i testen.
    }
}
