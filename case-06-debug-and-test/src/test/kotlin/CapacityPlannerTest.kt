import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CapacityPlannerTest {

    private val planner = CapacityPlanner()

    @Test
    fun `TODO same day should be clarified`() {
        // TODO:
        //  Skal from == to gi 0 eller 1?
        //  Avklar i testen.
    }

    @Test
    fun `should exclude weekends`() {
        val from = LocalDate.of(2026, 6, 1) // Monday
        val to = LocalDate.of(2026, 6, 8)   // Monday

        val result = planner.availableWorkingDays(from, to, emptySet())

        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `should exclude absence days`() {
        val from = LocalDate.of(2026, 6, 1)
        val to = LocalDate.of(2026, 6, 8)
        val absence = setOf(LocalDate.of(2026, 6, 3))

        val result = planner.availableWorkingDays(from, to, absence)

        assertThat(result).isEqualTo(4)
    }
}
