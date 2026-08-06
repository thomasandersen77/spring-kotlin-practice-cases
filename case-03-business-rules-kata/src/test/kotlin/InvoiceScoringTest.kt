import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class InvoiceScoringTest {

    private val scoring = InvoiceScoring()

    @Test
    fun `should calculate subtotal without discounts`() {
        val request = InvoiceRequest(
            lines = listOf(
                InvoiceLine(unitPrice = 100, quantity = 2),
                InvoiceLine(unitPrice = 50, quantity = 1)
            ),
            vipCustomer = false,
            discountCode = null
        )

        val result = scoring.score(request)

        assertThat(result.subtotal).isEqualTo(250)
        assertThat(result.discount).isEqualTo(0)
        assertThat(result.total).isEqualTo(250)
    }

    @Test
    fun `should combine vip and code discount`() {
        val request = InvoiceRequest(
            lines = listOf(InvoiceLine(unitPrice = 300, quantity = 1)),
            vipCustomer = true,
            discountCode = "SAVE50"
        )

        val result = scoring.score(request)

        assertThat(result.subtotal).isEqualTo(300)
        assertThat(result.discount).isEqualTo(80)
        assertThat(result.total).isEqualTo(220)
    }

    @Test
    fun `exercise clarify stacking rules for future discount codes`() {
        val result = scoring.score(
            InvoiceRequest(listOf(InvoiceLine(100, 2)), vipCustomer = true, discountCode = "SAVE10")
        )

        assertThat(result.discount).isEqualTo(40)
        assertThat(result.total).isEqualTo(160)
    }

    @Test
    fun `discount cannot make invoice total negative`() {
        val result = scoring.score(
            InvoiceRequest(listOf(InvoiceLine(20, 1)), vipCustomer = false, discountCode = "SAVE50")
        )

        assertThat(result.discount).isEqualTo(20)
        assertThat(result.total).isZero()
    }

    @Test
    fun `unknown discount code should fail explicitly`() {
        assertThatThrownBy {
            scoring.score(InvoiceRequest(listOf(InvoiceLine(100, 1)), false, "MISSING"))
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("unknown discount code")
    }

    @Test
    fun `empty invoice and invalid lines should be rejected`() {
        assertThatThrownBy { scoring.score(InvoiceRequest(emptyList(), false, null)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy {
            scoring.score(InvoiceRequest(listOf(InvoiceLine(-1, 1)), false, null))
        }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy {
            scoring.score(InvoiceRequest(listOf(InvoiceLine(1, 0)), false, null))
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
