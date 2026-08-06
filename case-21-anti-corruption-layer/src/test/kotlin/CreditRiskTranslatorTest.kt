import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
        val response = ExternalCreditScoreResponse("123", 499, false, "LEGACY")
        assertThat(translator.toCreditRisk(response)).isEqualTo(CreditRisk.HIGH)
    }

    @Test
    fun `translator should map medium and low boundaries and reject skewed score`() {
        assertThat(translator.toCreditRisk(ExternalCreditScoreResponse("1", 500, false, "X")))
            .isEqualTo(CreditRisk.MEDIUM)
        assertThat(translator.toCreditRisk(ExternalCreditScoreResponse("1", 700, false, "X")))
            .isEqualTo(CreditRisk.LOW)
        assertThatThrownBy {
            translator.toCreditRisk(ExternalCreditScoreResponse("1", 1001, false, "X"))
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `policy should approve reject and route manual review in domain language`() {
        val policy = CreditPolicy()

        assertThat(policy.decide(application(CreditRisk.LOW, "900000")).status)
            .isEqualTo(CreditDecisionStatus.APPROVED)
        assertThat(policy.decide(application(CreditRisk.HIGH, "100001")).status)
            .isEqualTo(CreditDecisionStatus.REJECTED)
        assertThat(policy.decide(application(CreditRisk.MEDIUM, "500001")).status)
            .isEqualTo(CreditDecisionStatus.MANUAL_REVIEW)
        assertThat(policy.decide(application(CreditRisk.HIGH, "100000")).status)
            .isEqualTo(CreditDecisionStatus.MANUAL_REVIEW)
    }

    private fun application(risk: CreditRisk, amount: String) = LoanApplication(
        java.util.UUID.randomUUID(), java.util.UUID.randomUUID(), java.math.BigDecimal(amount), risk
    )
}
