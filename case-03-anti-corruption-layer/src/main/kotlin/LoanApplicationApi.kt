import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/loan-applications")
class LoanApplicationController(
    private val service: LoanApplicationService
) {
    @PostMapping
    fun apply(@RequestBody request: LoanApplicationRequest): CreditDecision {
        return service.apply(request.applicantId, request.amount)
    }
}

data class LoanApplicationRequest(
    val applicantId: UUID,
    val amount: BigDecimal
)

class LoanApplicationService(
    private val externalCreditProviderClient: ExternalCreditProviderClient,
    private val creditRiskTranslator: CreditRiskTranslator
) {
    /**
     * TODO:
     *  - Denne klassen mangler @Service. Legg det til.
     *  - Bruk CreditPolicy
     *  - Lag bedre command/DTO-struktur
     *  - Skriv tester for mapping og policy
     */
    fun apply(applicantId: UUID, amount: BigDecimal): CreditDecision {
        val externalScore = externalCreditProviderClient.fetchCreditScore(applicantId)
        val risk = creditRiskTranslator.toCreditRisk(externalScore)

        val application = LoanApplication(
            id = UUID.randomUUID(),
            applicantId = applicantId,
            requestedAmount = amount,
            risk = risk
        )

        return CreditPolicy().decide(application)
    }
}
