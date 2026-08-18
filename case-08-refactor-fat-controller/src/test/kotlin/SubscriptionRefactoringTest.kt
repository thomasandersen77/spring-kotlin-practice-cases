import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [SubscriptionService::class])
class SubscriptionRefactoringTest {

	@Autowired lateinit var subscriptionService: SubscriptionService

	@Test
	fun `exercise extract pricing rules into domain model`() {

		val subscription1 = subscriptionService.create(mapOf("plan" to "BASIC"))

		assertNotNull(subscription1)
		assertEquals(Plan.BASIC, subscription1["plan"])
		assertEquals(99, subscription1["monthlyPrice"])
		// Neste steg i caset:
		// 1. Innfør SubscriptionPlan som uttrykksfullt domenebegrep.
		// 2. Flytt prisregler til en testbar policy/tjeneste uten HTTP-detaljer.
		// 3. Utvid testene med BASIC, PRO og ENTERPRISE, inkludert ett feiltilfelle.
	}

	@Test
	fun `exercise decide cancellation behavior for inactive subscription`() {
		// Beskriv forventet domenekontrakt i test først:
		// - enten idempotent (samme resultat ved gjentatt kall)
		// - eller eksplisitt avvisning med tydelig feilmelding/resultat
	}
}
