import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PricingServiceTest {

    @Test
    fun `basket subtotal should sum all lines`() {
        val basket = Basket(
            customerId = CustomerId("C1"),
            lines = listOf(
                BasketLine(ProductId("P1"), 2, Money(BigDecimal("100.00"))),
                BasketLine(ProductId("P2"), 1, Money(BigDecimal("50.00")))
            )
        )

        assertThat(basket.subtotal().amount).isEqualByComparingTo("250.00")
    }

    @Test
    fun `TODO percentage discount should reduce total`() {
        // TODO: Implementer PricingService.calculateTotal først.
    }

    @Test
    fun `TODO fixed discount should never make total negative`() {
        // TODO: Implementer regel.
    }
}
