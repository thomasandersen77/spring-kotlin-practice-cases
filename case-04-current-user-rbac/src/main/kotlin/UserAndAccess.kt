import org.springframework.stereotype.Component
import java.util.UUID

/**
 * SECURITY-MODELL
 *
 * Dette er bevisst forenklet og uten ekte Spring Security.
 *
 * TODO:
 *  - Diskuter hvordan dette kunne vært koblet til JWT
 *  - Diskuter CurrentUser-annotation i controller
 *  - Hold domenet testbart uten SecurityContextHolder
 */

annotation class CurrentUser

data class User(
    val id: UUID,
    val roles: Set<Role>,
    val organizationId: UUID
)

enum class Role {
    ADMIN,
    CASE_WORKER,
    READ_ONLY
}
@JvmInline
value class CaseId(val value: UUID)

data class CustomerCase(
    val id: CaseId,
    val organizationId: UUID,
    val status: CaseStatus
)

enum class CaseStatus {
    OPEN,
    CLOSED
}

@Component
class AccessPolicy {
    fun canCloseCase(user: User, customerCase: CustomerCase): Boolean {
        if (Role.ADMIN in user.roles) return true
        if (Role.READ_ONLY in user.roles) return false

        return Role.CASE_WORKER in user.roles &&
            user.organizationId == customerCase.organizationId &&
            customerCase.status == CaseStatus.OPEN
    }
}
