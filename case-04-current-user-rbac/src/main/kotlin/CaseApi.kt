import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/cases")
class CaseController(
    private val service: CaseService
) {
    /**
     * TODO:
     *  - I ekte app: @CurrentUser user: CurrentUser
     *  - For øving: send userId/role i request eller header
     *  - Hold controller tynn
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
     * TODO:
     *  - Legg til @Service
     *  - Hent sak fra repository
     *  - Bruk AccessPolicy
     *  - Kast ForbiddenException ved manglende tilgang
     *  - Skriv test for access policy
     */
    fun closeCase(user: User, caseId: CaseId) {


        // TODO("Implementer lukking av sak")

    }
}
