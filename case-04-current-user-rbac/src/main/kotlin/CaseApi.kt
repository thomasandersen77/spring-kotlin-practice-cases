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
        @PathVariable caseId: UUID,
        @RequestBody request: CloseCaseRequest
    ): CloseCaseResponse {
        val user = CurrentUser(
            id = request.userId,
            roles = request.roles,
            organizationId = request.organizationId
        )

        service.closeCase(user, CaseId(caseId))
        return CloseCaseResponse(caseId, "CLOSED")
    }
}

data class CloseCaseRequest(
    val userId: UUID,
    val organizationId: UUID,
    val roles: Set<Role>
)

data class CloseCaseResponse(
    val caseId: UUID,
    val status: String
)

class CaseService {
    private val accessPolicy = AccessPolicy()

    /**
     * TODO:
     *  - Legg til @Service
     *  - Hent sak fra repository
     *  - Bruk AccessPolicy
     *  - Kast ForbiddenException ved manglende tilgang
     *  - Skriv test for access policy
     */
    fun closeCase(user: CurrentUser, caseId: CaseId) {
        TODO("Implementer lukking av sak")
    }
}
