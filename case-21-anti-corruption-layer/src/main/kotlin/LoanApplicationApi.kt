import org.springframework.web.bind.annotation.*
import org.springframework.stereotype.Service
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

@Service
class LoanApplicationService(
    private val externalCreditProviderClient: ExternalCreditProviderClient,
    private val creditRiskTranslator: CreditRiskTranslator,
    private val creditPolicy: CreditPolicy
) {
    /**
     * Neste steg i caset:
     *  - Marker klassen som application service i Spring-laget.
     *  - Injektér og bruk CreditPolicy i stedet for å opprette den inline.
     *  - Hold API-request adskilt fra intern command-modell ved behov.
     *  - Skriv separate tester for ACL-mapping, policy og orkestrering.
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

        return creditPolicy.decide(application)
    }
}
