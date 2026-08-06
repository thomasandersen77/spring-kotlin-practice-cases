import java.math.BigDecimal
import java.util.UUID
import org.springframework.stereotype.Component

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
    val reason: String,
    val status: CreditDecisionStatus = if (approved) CreditDecisionStatus.APPROVED else CreditDecisionStatus.REJECTED
)

enum class CreditDecisionStatus { APPROVED, REJECTED, MANUAL_REVIEW }

/**
 * Neste steg i caset:
 *  - Modellér beslutningsregler eksplisitt i domenespråk:
 *      HIGH risk over 100 000 -> avslag
 *      MEDIUM risk over 500 000 -> manuell behandling
 *      LOW risk -> godkjenning
 *  - Forklar i kode/test hvorfor dette er domeneregel og ikke integrasjonsregel.
 */
@Component
class CreditPolicy {
    fun decide(application: LoanApplication): CreditDecision {
        require(application.requestedAmount.signum() > 0) { "requested amount must be positive" }
        return when {
            application.risk == CreditRisk.LOW -> decision(application, CreditDecisionStatus.APPROVED, "Low credit risk")
            application.risk == CreditRisk.HIGH && application.requestedAmount > BigDecimal("100000") ->
                decision(application, CreditDecisionStatus.REJECTED, "High risk above 100000")
            application.risk == CreditRisk.MEDIUM && application.requestedAmount <= BigDecimal("500000") ->
                decision(application, CreditDecisionStatus.APPROVED, "Medium risk within automatic limit")
            else -> decision(application, CreditDecisionStatus.MANUAL_REVIEW, "Manual credit review required")
        }
    }

    private fun decision(application: LoanApplication, status: CreditDecisionStatus, reason: String) =
        CreditDecision(application.id, status == CreditDecisionStatus.APPROVED, reason, status)
}
