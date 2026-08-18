import java.math.BigDecimal
import java.util.UUID

/**
 * DOMAIN
 *
 * Dette er kjernen. Den skal ikke kjenne til navnene/feltene fra ekstern leverandør.
 */
data class LoanApplication(
	val id: UUID,
	val applicantId: UUID,
	val requestedAmount: BigDecimal,
	val risk: CreditRisk,
)

enum class CreditRisk {
	LOW,
	MEDIUM,
	HIGH,
}

data class CreditDecision(
	val applicationId: UUID,
	val approved: Boolean,
	val reason: String,
)

/**
 * Neste steg i caset:
 * - Modellér beslutningsregler eksplisitt i domenespråk: HIGH risk over 100 000 -> avslag MEDIUM
 *   risk over 500 000 -> manuell behandling LOW risk -> godkjenning
 * - Forklar i kode/test hvorfor dette er domeneregel og ikke integrasjonsregel.
 */
class CreditPolicy {
	fun decide(application: LoanApplication): CreditDecision {
		TODO("Implement domain policy with explicit thresholds and readable decision reasons")
	}
}
