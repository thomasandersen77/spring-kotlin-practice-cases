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
    fun `TODO low score should become high risk`() {
        // TODO: Implementer test.
    }
}
