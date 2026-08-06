import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class SubscriptionService(
    private val repository: SubscriptionRepository? = null
) {
    fun create(command: CreateSubscriptionCommand): Subscription {
        val subscription = Subscription(
            id = UUID.randomUUID(),
            customerId = command.customerId,
            plan = command.plan,
            active = true,
            createdDate = LocalDate.now()
        )
        return repository?.save(subscription) ?: subscription
    }

    fun create(request: Map<String, String>): Map<String, Any> {
        val planName = request["plan"] ?: throw IllegalArgumentException("Missing plan")
        val plan = try {
            Plan.valueOf(planName.uppercase())
        } catch (e: Exception) {
            throw IllegalArgumentException("Unknown plan: $planName")
        }

        val customerId = request["customerId"]?.let(::parseCustomerId)
        return create(CreateSubscriptionCommand(customerId, plan)).toLegacyResponse()
    }

    fun cancelSubscription(id: UUID): Subscription {
        val repo = repository ?: throw IllegalStateException("Repository not available")
        val subscription = repo.findById(id) ?: throw NoSuchElementException("Subscription $id not found")
        return if (!subscription.active) subscription else repo.save(subscription.cancel())
    }

    fun cancel(id: UUID): Map<String, Any> = cancelSubscription(id).toLegacyResponse()

    private fun parseCustomerId(value: String): UUID = try {
        UUID.fromString(value)
    } catch (_: IllegalArgumentException) {
        throw IllegalArgumentException("Invalid customerId: $value")
    }
}

enum class Plan(val monthlyPrice: Int) {
    BASIC(99),
    PRO(199),
    ENTERPRISE(499)
}

data class CreateSubscriptionCommand(val customerId: UUID?, val plan: Plan)

data class Subscription(
    val id: UUID,
    val customerId: UUID?,
    val plan: Plan,
    val active: Boolean,
    val createdDate: LocalDate
) {
    fun cancel(): Subscription = if (active) copy(active = false) else this
}

private fun Subscription.toLegacyResponse(): Map<String, Any> = mapOf(
    "id" to id.toString(), "plan" to plan, "monthlyPrice" to plan.monthlyPrice,
    "active" to active, "createdDate" to createdDate
)

interface SubscriptionRepository {
    fun save(subscription: Subscription): Subscription
    fun findById(id: UUID): Subscription?
}

interface SubscriptionJpaRepository : JpaRepository<SubscriptionEntity, UUID>

@Repository
class JpaSubscriptionRepository(private val jpa: SubscriptionJpaRepository) : SubscriptionRepository {
    override fun save(subscription: Subscription): Subscription = jpa.save(subscription.toEntity()).toDomain()
    override fun findById(id: UUID): Subscription? = jpa.findById(id).orElse(null)?.toDomain()
}

private fun Subscription.toEntity() = SubscriptionEntity(
    id, requireNotNull(customerId) { "customerId is required for persistence" },
    plan.name, plan.monthlyPrice, active, createdDate
)

private fun SubscriptionEntity.toDomain() = Subscription(id, customerId, Plan.valueOf(plan), active, createdDate)
