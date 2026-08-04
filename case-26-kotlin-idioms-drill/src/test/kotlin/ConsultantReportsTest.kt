import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ConsultantReportsTest {

    private val reports = ConsultantReports()

    private val kari = Consultant("Kari", "Oslo", 1600, 12, listOf("Kotlin", "Spring Boot"))
    private val ola = Consultant("Ola", "Bergen", 1200, 5, listOf("Kotlin", "React"))
    private val liv = Consultant("Liv", null, 1800, 20, listOf("Arkitektur", "Kotlin"))
    private val per = Consultant("Per", "Oslo", 900, 1, listOf("React"))

    @Test
    fun `seniors sorted by rate descending`() {
        val names = reports.namesOfSeniorsSortedByRate(listOf(kari, ola, liv, per))
        assertThat(names).containsExactly("Liv", "Kari")
    }

    @Test
    fun `describe location handles null consultant, null city and blank city`() {
        assertThat(reports.describeLocation(null)).isEqualTo("ingen konsulent")
        assertThat(reports.describeLocation(liv)).isEqualTo("Liv har ukjent lokasjon")
        assertThat(reports.describeLocation(kari)).isEqualTo("Kari jobber fra Oslo")
        assertThat(reports.describeLocation(kari.copy(city = "  "))).isEqualTo("Kari har ukjent lokasjon")
    }

    @Test
    fun `skill index maps skill to consultant names`() {
        val index = reports.buildSkillIndex(listOf(kari, ola, liv, per))
        assertThat(index["Kotlin"]).containsExactly("Kari", "Ola", "Liv")
        assertThat(index["React"]).containsExactly("Ola", "Per")
        assertThat(index["Arkitektur"]).containsExactly("Liv")
    }

    @Test
    fun `total daily cost sums hourly rates`() {
        assertThat(reports.totalDailyCost(listOf(kari, ola), 8)).isEqualTo((1600 + 1200) * 8)
        assertThat(reports.totalDailyCost(emptyList(), 8)).isZero()
    }

    @Test
    fun `seniority labels`() {
        assertThat(kari.seniority).isEqualTo(Seniority.SENIOR)
        assertThat(ola.seniority).isEqualTo(Seniority.ERFAREN)
        assertThat(liv.seniority).isEqualTo(Seniority.VETERAN)
        assertThat(per.seniority).isEqualTo(Seniority.JUNIOR)
        assertThatThrownBy { reports.seniority(-1) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `summary line joins names in city regardless of casing`() {
        val line = reports.summaryLine(listOf(kari, ola, liv, per), "Oslo")
        assertThat(line).isEqualTo("Konsulenter i Oslo: Kari, Per")
    }
}
