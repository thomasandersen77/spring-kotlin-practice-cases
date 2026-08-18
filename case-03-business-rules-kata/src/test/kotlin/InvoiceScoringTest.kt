import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class InvoiceScoringTest {

	private val scoring = InvoiceScoring()

	@Test
	fun `should calculate subtotal without discounts`() {
		val request =
			InvoiceRequest(
				lines =
					listOf(
						InvoiceLine(unitPrice = 100, quantity = 2),
						InvoiceLine(unitPrice = 50, quantity = 1),
					),
				vipCustomer = false,
				discountCode = null,
			)

		val result = scoring.score(request)

		assertThat(result.subtotal).isEqualTo(250)
		assertThat(result.discount).isEqualTo(0)
		assertThat(result.total).isEqualTo(250)
	}

	@Test
	fun `should combine vip and code discount`() {
		val request =
			InvoiceRequest(
				lines = listOf(InvoiceLine(unitPrice = 300, quantity = 1)),
				vipCustomer = true,
				discountCode = "SAVE50",
			)

		val result = scoring.score(request)

		assertThat(result.subtotal).isEqualTo(300)
		assertThat(result.discount).isEqualTo(80)
		assertThat(result.total).isEqualTo(220)
	}

	@Test
	fun `exercise clarify stacking rules for future discount codes`() {
		// Dokumenter ønsket regelrekkefølge med tester før implementasjon:
		// - skal VIP kombineres med kode?
		// - hvis ja: i hvilken rekkefølge?
		// - hvis nei: hvilken regel vinner?
	}
}
