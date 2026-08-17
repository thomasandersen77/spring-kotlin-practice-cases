import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CreditRiskTranslatorTest {

    private val translator = CreditRiskTranslator()

    @Test
    fun `red flag should always become high risk`() {
        val response = ExternalCreditScoreResponse(
            person_ref = "123",
            score_value = 900,
            red_flag = true,
            source_system = "LEGACY"
        )

        assertThat(translator.toCreditRisk(response)).isEqualTo(CreditRisk.HIGH)
    }

    @Test
    fun `exercise low score should become high risk`() {
        // Legg inn test for score under valgt terskel og dokumenter terskelen tydelig i testnavn eller testdata.
    }
}
