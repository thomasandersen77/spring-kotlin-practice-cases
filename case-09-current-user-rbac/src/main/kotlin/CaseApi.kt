import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/cases")
class CaseController(
    private val service: CaseService
) {
    /**
     * Neste steg i caset:
     *  - I ekte app: map autentisert principal til User i adapterlaget.
     *  - I øving: simuler CurrentUser uten å dra policylogikk inn i controller.
     *  - Hold controller tynn og delegér til service/use case.
     */
    @PostMapping("/{caseId}/close")
    fun closeCase(
        @RequestBody request: CloseCaseRequest,
        @CurrentUser user: User
    ): CloseCaseResponse {
        val caseId = CaseId(request.caseId)
        service.closeCase(user, caseId)
        return CloseCaseResponse(caseId,CaseStatus.CLOSED)
    }
}

data class CloseCaseRequest(
    val caseId: UUID,
)

data class CloseCaseResponse(
    val caseId: CaseId,
    val status: CaseStatus
)

@Service
class CaseService(
    val accessPolicy: AccessPolicy
) {
    /**
     * Neste steg i caset:
     *  - Les sak fra repository-port eller testbar in-memory adapter.
     *  - Bruk AccessPolicy før statusendring.
     *  - Modellér manglende tilgang eksplisitt (exception eller resultat-type).
     *  - Skriv tester som dekker både autorisert og uautorisert flyt.
     */
    fun closeCase(user: User, caseId: CaseId) {


        // Implementer use case-flyt: hent sak -> valider tilgang -> utfør lukking -> persistér.

    }
}
