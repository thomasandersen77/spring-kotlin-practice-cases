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
        @PathVariable caseId: UUID,
        @CurrentUser user: User
    ): CloseCaseResponse {
        val closed = service.closeCase(user, CaseId(caseId))
        return CloseCaseResponse(closed.id, closed.status)
    }
}

data class CloseCaseResponse(
    val caseId: CaseId,
    val status: CaseStatus
)

@Service
class CaseService(
    private val accessPolicy: AccessPolicy,
    private val repository: CaseRepository
) {
    fun closeCase(user: User, caseId: CaseId): CustomerCase {
        val customerCase = repository.findById(caseId) ?: throw NoSuchElementException("Case ${caseId.value} not found")
        if (!accessPolicy.canCloseCase(user, customerCase)) throw CaseAccessDeniedException(caseId)
        return repository.save(customerCase.close())
    }
}

class CaseAccessDeniedException(caseId: CaseId) : RuntimeException("Access denied for case ${caseId.value}")

interface CaseRepository {
    fun findById(id: CaseId): CustomerCase?
    fun save(customerCase: CustomerCase): CustomerCase
}
