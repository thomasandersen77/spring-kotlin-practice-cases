import org.springframework.stereotype.Component
import java.util.UUID

/**
 * Simulert ekstern tjeneste.
 *
 * Dette er "skittent" eksternt språk.
 * Ikke la disse DTO-ene lekke inn i domain/core.
 */
data class ExternalCreditScoreResponse(
    val person_ref: String,
    val score_value: Int,
    val red_flag: Boolean,
    val source_system: String
)

@Component
class ExternalCreditProviderClient {
    fun fetchCreditScore(applicantId: UUID): ExternalCreditScoreResponse {
        // Simulert respons.
        return ExternalCreditScoreResponse(
            person_ref = applicantId.toString(),
            score_value = 742,
            red_flag = false,
            source_system = "LEGACY_CREDIT_PROVIDER"
        )
    }
}

/**
 * ANTI-CORRUPTION LAYER
 *
 * Neste steg i caset:
 *  - Utvid mappingen med eksplisitt håndtering av grensetilfeller (ukjent/skjev score).
 *  - Beskriv med tester hvorfor leverandørfelter ikke skal lekke inn i domenet.
 *  - Hold leverandørspesifikke beslutninger samlet i ACL-laget.
 */
@Component
class CreditRiskTranslator {
    fun toCreditRisk(response: ExternalCreditScoreResponse): CreditRisk {
        if (response.red_flag) return CreditRisk.HIGH

        return when {
            response.score_value >= 700 -> CreditRisk.LOW
            response.score_value >= 500 -> CreditRisk.MEDIUM
            else -> CreditRisk.HIGH
        }
    }
}
