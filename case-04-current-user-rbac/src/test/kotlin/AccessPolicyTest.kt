import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class AccessPolicyTest {

    private val policy = AccessPolicy()

    @Test
    fun `admin can close any case`() {
        val user = User(
            id = UUID.randomUUID(),
            roles = setOf(Role.ADMIN),
            organizationId = UUID.randomUUID()
        )

        val customerCase = CustomerCase(
            id = CaseId(UUID.randomUUID()),
            organizationId = UUID.randomUUID(),
            status = CaseStatus.OPEN
        )

        assertThat(policy.canCloseCase(user, customerCase)).isTrue()
    }

    @Test
    fun `TODO read only user cannot close case`() {

        val readOnlyUser = User(
            id = UUID.randomUUID(),
            roles = setOf(Role.READ_ONLY),
            organizationId = UUID.randomUUID()
        )

        val customerCase = CustomerCase(
            id = CaseId(UUID.randomUUID()),
            organizationId = readOnlyUser.organizationId,
            status = CaseStatus.OPEN
        )

        assertThat(policy.canCloseCase(readOnlyUser, customerCase)).isFalse()
    }
}
