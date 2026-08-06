import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class SubscriptionRefactoringTest {

    private val subscriptionService = SubscriptionService()
    @Test
    fun `exercise extract pricing rules into domain model`() {

        val subscription1 = subscriptionService.create(mapOf("plan" to "BASIC"))

        assertNotNull(subscription1)
        assertEquals(Plan.BASIC, subscription1["plan"])
        assertEquals(99, subscription1["monthlyPrice"])
        assertEquals(199, Plan.PRO.monthlyPrice)
        assertEquals(499, Plan.ENTERPRISE.monthlyPrice)
        assertThatThrownBy { subscriptionService.create(mapOf("plan" to "UNKNOWN")) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `exercise decide cancellation behavior for inactive subscription`() {
        val subscription = Subscription(UUID.randomUUID(), null, Plan.PRO, true, LocalDate.parse("2026-01-01"))

        assertEquals(false, subscription.cancel().active)
        assertEquals(subscription.cancel(), subscription.cancel().cancel())
    }

    @Test
    fun `invalid customer id should have explicit error`() {
        assertThatThrownBy {
            subscriptionService.create(mapOf("plan" to "BASIC", "customerId" to "not-a-uuid"))
        }.isInstanceOf(IllegalArgumentException::class.java).hasMessageContaining("customerId")
    }
}
