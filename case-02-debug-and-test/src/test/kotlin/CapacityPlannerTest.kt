import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class CapacityPlannerTest {

    private val planner = CapacityPlanner()

    @Test
    fun `exercise clarify same-day semantics`() {
        // Beskriv kontrakten eksplisitt:
        // - Skal from == to gi 0 eller 1 tilgjengelig arbeidsdag?
        // - Hvordan henger valget sammen med inclusive/exclusive-regelen i resten av perioden?

        // to dato er eksklusiv og telles ikke
        val from = LocalDate.of(2026, Month.JUNE, 1)
        val to = LocalDate.of(2026, Month.JUNE, 1)

        val availableWorkingDays = planner.availableWorkingDays(from, to, setOf())
        assertThat(availableWorkingDays).isEqualTo(0)

    }

    @Test
    fun `should exclude weekends`() {
        val from = LocalDate.of(2026, Month.JUNE, 1) // Monday
        val to = LocalDate.of(2026, Month.JUNE, 8)   // Monday

        val result = planner.availableWorkingDays(from, to, emptySet())

        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `should exclude absence days`() {
        val from = LocalDate.of(2026, Month.JUNE, 1)
        val to = LocalDate.of(2026, Month.JUNE, 8)
        val absence = setOf(LocalDate.of(2026, Month.JUNE, 3))

        val result = planner.availableWorkingDays(from, to, absence)

        assertThat(result).isEqualTo(4)
    }

    @Test
    fun `period starting on weekend counts only following workdays`() {
        val from = LocalDate.of(2026, Month.JUNE, 6) // Saturday
        val to = LocalDate.of(2026, Month.JUNE, 9)   // Tuesday

        val result = planner.availableWorkingDays(from, to, emptySet())

        assertThat(result).isEqualTo(1) // only Monday 2026-06-08
    }

    @Test
    fun `period ending on weekend excludes the weekend`() {
        val from = LocalDate.of(2026, Month.JUNE, 5) // Friday
        val to = LocalDate.of(2026, Month.JUNE, 8)   // Monday

        val result = planner.availableWorkingDays(from, to, emptySet())

        assertThat(result).isEqualTo(1) // only Friday
    }

    @Test
    fun `absence on a weekend day does not reduce the count`() {
        val from = LocalDate.of(2026, Month.JUNE, 1)
        val to = LocalDate.of(2026, Month.JUNE, 8)
        val absence = setOf(LocalDate.of(2026, Month.JUNE, 6)) // Saturday

        val result = planner.availableWorkingDays(from, to, absence)

        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `absence outside the period does not affect the count`() {
        val from = LocalDate.of(2026, Month.JUNE, 1)
        val to = LocalDate.of(2026, Month.JUNE, 8)
        val absence = setOf(LocalDate.of(2026, Month.JULY, 1))

        val result = planner.availableWorkingDays(from, to, absence)

        assertThat(result).isEqualTo(5)
    }

    @Test
    fun `from after to is rejected as a caller error`() {
        val from = LocalDate.of(2026, Month.JUNE, 8)
        val to = LocalDate.of(2026, Month.JUNE, 1)

        assertThatThrownBy { planner.availableWorkingDays(from, to, emptySet()) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}
