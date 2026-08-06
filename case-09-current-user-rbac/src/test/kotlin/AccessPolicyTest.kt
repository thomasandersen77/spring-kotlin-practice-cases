import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
    fun `exercise read only user cannot close case`() {

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

    @Test
    fun `case worker can only close open case in own organization`() {
        val organizationId = UUID.randomUUID()
        val worker = User(UUID.randomUUID(), setOf(Role.CASE_WORKER), organizationId)

        assertThat(policy.canCloseCase(worker, customerCase(organizationId, CaseStatus.OPEN))).isTrue()
        assertThat(policy.canCloseCase(worker, customerCase(UUID.randomUUID(), CaseStatus.OPEN))).isFalse()
        assertThat(policy.canCloseCase(worker, customerCase(organizationId, CaseStatus.CLOSED))).isFalse()
    }

    @Test
    fun `service should authorize close and persist transition`() {
        val organizationId = UUID.randomUUID()
        val existing = customerCase(organizationId, CaseStatus.OPEN)
        val repository = InMemoryCaseRepository(existing)
        val service = CaseService(policy, repository)

        val closed = service.closeCase(
            User(UUID.randomUUID(), setOf(Role.CASE_WORKER), organizationId), existing.id
        )

        assertThat(closed.status).isEqualTo(CaseStatus.CLOSED)
        assertThat(repository.findById(existing.id)).isEqualTo(closed)
    }

    @Test
    fun `service should reject unauthorized close`() {
        val existing = customerCase(UUID.randomUUID(), CaseStatus.OPEN)
        val service = CaseService(policy, InMemoryCaseRepository(existing))

        assertThatThrownBy {
            service.closeCase(User(UUID.randomUUID(), setOf(Role.READ_ONLY), existing.organizationId), existing.id)
        }.isInstanceOf(CaseAccessDeniedException::class.java)
    }

    private fun customerCase(organizationId: UUID, status: CaseStatus) =
        CustomerCase(CaseId(UUID.randomUUID()), organizationId, status)

    private class InMemoryCaseRepository(initial: CustomerCase) : CaseRepository {
        private var stored = initial
        override fun findById(id: CaseId): CustomerCase? = stored.takeIf { it.id == id }
        override fun save(customerCase: CustomerCase): CustomerCase = customerCase.also { stored = it }
    }
}
