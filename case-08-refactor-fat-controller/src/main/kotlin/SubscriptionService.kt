import java.time.LocalDate
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
class SubscriptionService(val repository: SubscriptionJpaRepository? = null) {

	fun create(request: Map<String, String>): Map<String, Any> {
		val planName = request["plan"] ?: throw IllegalArgumentException("Missing plan")
		val plan =
			try {
				Plan.valueOf(planName.uppercase())
			} catch (e: Exception) {
				throw IllegalArgumentException("Unknown plan: $planName")
			}

		val monthlyPrice = plan.monthlyPrice

		val subscription =
			mapOf<String, Any>(
				"id" to UUID.randomUUID().toString(),
				"plan" to plan,
				"monthlyPrice" to monthlyPrice,
				"active" to true,
				"createdDate" to LocalDate.now(),
			)

		val repo = repository
		if (repo != null && request.containsKey("customerId")) {
			val customerId = UUID.fromString(request["customerId"])
			val entity =
				SubscriptionEntity(
					id = UUID.randomUUID(),
					customerId = customerId,
					plan = plan.name,
					monthlyPrice = monthlyPrice,
					active = true,
					createdDate = LocalDate.now(),
				)
			repo.save(entity)
		}

		return subscription
	}

	fun cancel(id: UUID): Map<String, Any> {
		val repo = repository ?: throw IllegalStateException("Repository not available")
		val entity = repo.findById(id).orElseThrow()
		entity.active = false
		val saved = repo.save(entity)

		return mapOf(
			"id" to saved.id.toString(),
			"active" to saved.active,
		)
	}
}

enum class Plan(val monthlyPrice: Int) {
	BASIC(99),
	PRO(199),
	ENTERPRISE(499),
}

interface SubscriptionJpaRepository : JpaRepository<SubscriptionEntity, UUID>
