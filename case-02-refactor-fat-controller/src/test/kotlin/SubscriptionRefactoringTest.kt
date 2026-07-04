import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [SubscriptionService::class])
class SubscriptionRefactoringTest {

    @Autowired
    lateinit var subscriptionService: SubscriptionService
    @Test
    fun `TODO refactor pricing rules into domain`() {

        val subscription1 = subscriptionService.create(mapOf("plan" to "BASIC"))

        assertNotNull(subscription1)
        assertEquals(Plan.BASIC, subscription1["plan"])
        assertEquals(99, subscription1["monthlyPrice"])
        // TODO:
        //  1. Lag enum SubscriptionPlan
        //  2. Flytt prisregel ut av controller
        //  3. Test BASIC, PRO og ENTERPRISE
    }

    @Test
    fun `TODO cancelling inactive subscription should be idempotent or fail explicitly`() {
        // TODO:
        //  Diskuter hva riktig domeneoppførsel skal være.
    }
}
