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
    val risk: CreditRisk
)

enum class CreditRisk {
    LOW,
    MEDIUM,
    HIGH
}

data class CreditDecision(
    val applicationId: UUID,
    val approved: Boolean,
    val reason: String
)

/**
 * TODO:
 *  - Legg til domene-regel:
 *      HIGH risk over 100 000 skal avslås
 *      MEDIUM risk over 500 000 skal manuellbehandles
 *      LOW risk kan godkjennes
 */
class CreditPolicy {
    fun decide(application: LoanApplication): CreditDecision {
        TODO("Implementer domenereglene")
    }
}
