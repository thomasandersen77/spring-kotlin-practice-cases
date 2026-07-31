import Discount
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PricingServiceTest {

    @Test
    fun `basket subtotal should sum all lines`() {
        val basket = Basket(
            customerId = CustomerId("C1"),
            lines = listOf(
                BasketLine(ProductId("P1"), Quantity(2), Money(BigDecimal("100.00"))),
                BasketLine(ProductId("P2"), Quantity(1), Money(BigDecimal("50.00")))
            )
        )

        assertThat(basket.subtotal().amount).isEqualByComparingTo("250.00")
    }

    @Test
    fun `exercise percentage discount should reduce total`() {

        val basket = Basket(
            customerId = CustomerId("C1"),
            lines = listOf(
                BasketLine(ProductId("P1"), Quantity(1), Money(BigDecimal("50.00"))),
                BasketLine(ProductId("P2"), Quantity(1), Money(BigDecimal("50.00")))
            )
        )

        val ps = PricingService()
        val total = ps.calculateTotal(basket, Discount.Percentage(10))
        assertThat(total.amount).isEqualByComparingTo(BigDecimal.valueOf(90))


    }

    @Test
    fun `exercise fixed discount should never make total negative`() {

        val basket = Basket(
            customerId = CustomerId("C1"),
            lines = listOf(
                BasketLine(ProductId("P1"), Quantity(1), Money(BigDecimal("50.00"))),
                BasketLine(ProductId("P2"), Quantity(1), Money(BigDecimal("50.00")))
            )
        )
        val ps = PricingService()
        val total = ps.calculateTotal(basket, Discount.FixedAmount(Money(BigDecimal("100"))))
        assertThat(total.amount).isEqualByComparingTo(BigDecimal.valueOf(0))

        val total2 = ps.calculateTotal(basket, Discount.FixedAmount(Money(BigDecimal("150"))))
        assertThat(total2.amount).isEqualByComparingTo(BigDecimal.valueOf(0))
    }
    @Test
    fun `no discount should return subtotal`() {
        val basket = Basket(
            customerId = CustomerId("C1"),
            lines = listOf(
                BasketLine(ProductId("P1"), Quantity(1), Money(BigDecimal("50.00"))),
                BasketLine(ProductId("P2"), Quantity(1), Money(BigDecimal("200.00")))
            )
        )
        val ps = PricingService()
        val total = ps.calculateTotal(basket, Discount.NoDiscount)
        assertThat(total.amount).isEqualByComparingTo(basket.subtotal().amount)
    }
}
