import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
        assertThatThrownBy { Order(UUID.randomUUID(), UUID.randomUUID(), emptyList()) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("at least one")
    }

    @Test
    fun `order line should require positive quantity and price`() {
        assertThatThrownBy { OrderLine(UUID.randomUUID(), 0, BigDecimal.ONE) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { OrderLine(UUID.randomUUID(), 1, BigDecimal.ZERO) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `only pending order can be confirmed`() {
        val pending = order()
        val confirmed = pending.confirm()

        assertThat(confirmed.status).isEqualTo(OrderStatus.CONFIRMED)
        assertThatThrownBy { confirmed.confirm() }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `pending and confirmed orders can be cancelled but cancelled order cannot transition`() {
        assertThat(order().cancel().status).isEqualTo(OrderStatus.CANCELLED)
        val cancelled = order().confirm().cancel()
        assertThatThrownBy { cancelled.cancel() }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { cancelled.confirm() }.isInstanceOf(IllegalArgumentException::class.java)
    }

    private fun order() = Order(
        UUID.randomUUID(), UUID.randomUUID(), listOf(OrderLine(UUID.randomUUID(), 1, BigDecimal.TEN))
    )
}
