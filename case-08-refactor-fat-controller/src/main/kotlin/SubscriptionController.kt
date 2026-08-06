import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.UUID

/**
 * MED VILJE ROTETE KODE.
 *
 * Intervjucase:
 *  - Refaktorer denne controlleren
 *  - Flytt forretningsregler ut i domene/service
 *  - Lag DTO-er
 *  - Lag repository-port om du vil
 *  - Legg til tester
 *  - Diskuter hva du ville gjort hvis casen var tidsbegrenset
 */
@RestController
@RequestMapping("/subscriptions")
class SubscriptionController(
    private val subscriptionService: SubscriptionService
) {
    @PostMapping
    fun create(@RequestBody request: CreateSubscriptionRequest): SubscriptionResponse =
        subscriptionService.create(request.toCommand()).toResponse()

    @PostMapping("/{id}/cancel")
    fun cancel(@PathVariable id: UUID): SubscriptionResponse =
        subscriptionService.cancelSubscription(id).toResponse()
}

data class CreateSubscriptionRequest(val customerId: String?, val plan: String) {
    fun toCommand(): CreateSubscriptionCommand {
        val parsedPlan = try { Plan.valueOf(plan.trim().uppercase()) }
        catch (_: IllegalArgumentException) { throw IllegalArgumentException("Unknown plan: $plan") }
        val parsedCustomer = customerId?.let {
            try { UUID.fromString(it) }
            catch (_: IllegalArgumentException) { throw IllegalArgumentException("Invalid customerId: $it") }
        }
        return CreateSubscriptionCommand(parsedCustomer, parsedPlan)
    }
}

data class SubscriptionResponse(
    val id: UUID, val customerId: UUID?, val plan: Plan, val monthlyPrice: Int,
    val active: Boolean, val createdDate: LocalDate
)

fun Subscription.toResponse() = SubscriptionResponse(id, customerId, plan, plan.monthlyPrice, active, createdDate)

@Entity
@Table(name = "subscriptions")
class SubscriptionEntity(
    @Id
    var id: UUID = UUID.randomUUID(),
    var customerId: UUID = UUID.randomUUID(),
    var plan: String = "",
    var monthlyPrice: Int = 0,
    var active: Boolean = true,
    var createdDate: LocalDate = LocalDate.now()
)

