import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.util.UUID
import org.springframework.web.bind.annotation.*

/**
 * MED VILJE ROTETE KODE.
 *
 * Treningscase:
 * - Refaktorer denne controlleren
 * - Flytt forretningsregler ut i domene/service
 * - Lag DTO-er
 * - Lag repository-port om du vil
 * - Legg til tester
 * - Diskuter hva du ville gjort hvis casen var tidsbegrenset
 */
@RestController
@RequestMapping("/subscriptions")
class SubscriptionController(private val subscriptionService: SubscriptionService) {
	@PostMapping
	fun create(@RequestBody request: Map<String, String>): Map<String, Any> {
		return subscriptionService.create(request)
	}

	@PostMapping("/{id}/cancel")
	fun cancel(@PathVariable id: UUID): Map<String, Any> {
		return subscriptionService.cancel(id)
	}
}

@Entity
@Table(name = "subscriptions")
class SubscriptionEntity(
	@Id var id: UUID = UUID.randomUUID(),
	var customerId: UUID = UUID.randomUUID(),
	var plan: String = "",
	var monthlyPrice: Int = 0,
	var active: Boolean = true,
	var createdDate: LocalDate = LocalDate.now(),
)
