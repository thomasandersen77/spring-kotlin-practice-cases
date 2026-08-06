import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import java.time.LocalDate

class CapacityPlannerContractTest {

    private val planner = CapacityPlanner()

    @Test
    fun `period includes from and excludes to`() {
        val monday = LocalDate.of(2026, 6, 1)
        val tuesday = monday.plusDays(1)

        assertThat(planner.availableWorkingDays(monday, tuesday, emptySet())).isEqualTo(1)
        assertThat(planner.availableWorkingDays(monday, monday, emptySet())).isZero()
    }

    @Test
    fun `from after to is rejected`() {
        val from = LocalDate.of(2026, 6, 2)
        val to = LocalDate.of(2026, 6, 1)

        assertThatIllegalArgumentException()
            .isThrownBy { planner.availableWorkingDays(from, to, emptySet()) }
            .withMessage("from must be on or before to")
    }

    @Test
    fun `weekend at either period boundary is not counted`() {
        val friday = LocalDate.of(2026, 6, 5)
        val saturday = LocalDate.of(2026, 6, 6)
        val monday = LocalDate.of(2026, 6, 8)
        val tuesday = LocalDate.of(2026, 6, 9)

        assertThat(planner.availableWorkingDays(friday, monday, emptySet())).isEqualTo(1)
        assertThat(planner.availableWorkingDays(saturday, tuesday, emptySet())).isEqualTo(1)
    }

    @Test
    fun `absence on weekend or outside period has no effect`() {
        val monday = LocalDate.of(2026, 6, 1)
        val nextMonday = LocalDate.of(2026, 6, 8)
        val absences = setOf(
            LocalDate.of(2026, 5, 29),
            LocalDate.of(2026, 6, 6),
            LocalDate.of(2026, 6, 8)
        )

        assertThat(planner.availableWorkingDays(monday, nextMonday, absences)).isEqualTo(5)
    }
}
