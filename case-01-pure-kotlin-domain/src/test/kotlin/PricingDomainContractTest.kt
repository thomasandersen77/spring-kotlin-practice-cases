import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PricingDomainContractTest {

    private val pricingService = PricingService()

    @Test
    fun `money rejects negative amounts but accepts zero`() {
        assertThat(Money(BigDecimal.ZERO)).isEqualTo(Money.ZERO)

        assertThatIllegalArgumentException()
            .isThrownBy { Money(BigDecimal("-0.01")) }
            .withMessage("Money cannot have a negative amount")
    }

    @Test
    fun `quantity must be greater than zero`() {
        assertThatIllegalArgumentException()
            .isThrownBy { Quantity(0) }
            .withMessage("Quantity must be greater than zero")

        assertThatIllegalArgumentException()
            .isThrownBy { Quantity(-1) }
            .withMessage("Quantity must be greater than zero")
    }

    @Test
    fun `percentage must be between zero and one hundred`() {
        assertThat(Discount.Percentage(0).percent).isZero()
        assertThat(Discount.Percentage(100).percent).isEqualTo(100)

        assertThatIllegalArgumentException()
            .isThrownBy { Discount.Percentage(-1) }
            .withMessage("Discount percent must be between 0 and 100")

        assertThatIllegalArgumentException()
            .isThrownBy { Discount.Percentage(101) }
            .withMessage("Discount percent must be between 0 and 100")
    }

    @Test
    fun `empty basket has zero total for every discount type`() {
        val basket = Basket(CustomerId("C1"), emptyList())

        assertThat(pricingService.calculateTotal(basket, Discount.NoDiscount).amount)
            .isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(pricingService.calculateTotal(basket, Discount.Percentage(25)).amount)
            .isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(pricingService.calculateTotal(basket, Discount.FixedAmount(Money(BigDecimal.TEN))).amount)
            .isEqualByComparingTo(BigDecimal.ZERO)
    }

    @Test
    fun `percentage discount rounds monetary result half up to two decimals`() {
        val basket = basketWithSingleLine(unitPrice = "10.05")

        val total = pricingService.calculateTotal(basket, Discount.Percentage(50))

        assertThat(total.amount).isEqualTo(BigDecimal("5.03"))
    }

    @Test
    fun `zero and full percentage discounts preserve boundary semantics`() {
        val basket = basketWithSingleLine(unitPrice = "19.99")

        assertThat(pricingService.calculateTotal(basket, Discount.Percentage(0)).amount)
            .isEqualByComparingTo("19.99")
        assertThat(pricingService.calculateTotal(basket, Discount.Percentage(100)).amount)
            .isEqualByComparingTo(BigDecimal.ZERO)
    }

    @Test
    fun `fixed discount handles amounts below equal to and above subtotal`() {
        val basket = basketWithSingleLine(unitPrice = "100.00")

        assertThat(pricingService.calculateTotal(basket, fixedDiscount("25.00")).amount)
            .isEqualByComparingTo("75.00")
        assertThat(pricingService.calculateTotal(basket, fixedDiscount("100.00")).amount)
            .isEqualByComparingTo(BigDecimal.ZERO)
        assertThat(pricingService.calculateTotal(basket, fixedDiscount("125.00")).amount)
            .isEqualByComparingTo(BigDecimal.ZERO)
    }

    private fun basketWithSingleLine(unitPrice: String): Basket = Basket(
        customerId = CustomerId("C1"),
        lines = listOf(
            BasketLine(
                productId = ProductId("P1"),
                quantity = Quantity(1),
                unitPrice = Money(BigDecimal(unitPrice))
            )
        )
    )

    private fun fixedDiscount(amount: String): Discount =
        Discount.FixedAmount(Money(BigDecimal(amount)))
}
