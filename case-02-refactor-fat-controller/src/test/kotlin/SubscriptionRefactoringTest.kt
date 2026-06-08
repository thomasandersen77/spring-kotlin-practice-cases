import org.junit.jupiter.api.Test

class SubscriptionRefactoringTest {

    @Test
    fun `TODO refactor pricing rules into domain`() {
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
