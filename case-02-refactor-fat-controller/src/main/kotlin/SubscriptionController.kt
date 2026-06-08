import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
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
    private val repository: SubscriptionJpaRepository
) {
    @PostMapping
    fun create(@RequestBody request: Map<String, String>): Map<String, Any> {
        val customerId = UUID.fromString(request["customerId"])
        val plan = request["plan"] ?: "BASIC"

        var monthlyPrice = 0
        if (plan == "BASIC") {
            monthlyPrice = 99
        }
        if (plan == "PRO") {
            monthlyPrice = 199
        }
        if (plan == "ENTERPRISE") {
            monthlyPrice = 499
        }

        if (monthlyPrice == 0) {
            throw IllegalArgumentException("Unknown plan")
        }

        val entity = SubscriptionEntity(
            id = UUID.randomUUID(),
            customerId = customerId,
            plan = plan,
            monthlyPrice = monthlyPrice,
            active = true,
            createdDate = LocalDate.now()
        )

        val saved = repository.save(entity)

        return mapOf(
            "id" to saved.id.toString(),
            "customerId" to saved.customerId.toString(),
            "plan" to saved.plan,
            "monthlyPrice" to saved.monthlyPrice,
            "active" to saved.active
        )
    }

    @PostMapping("/{id}/cancel")
    fun cancel(@PathVariable id: UUID): Map<String, Any> {
        val entity = repository.findById(id).orElseThrow()
        entity.active = false
        val saved = repository.save(entity)

        return mapOf(
            "id" to saved.id.toString(),
            "active" to saved.active
        )
    }
}

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

interface SubscriptionJpaRepository : JpaRepository<SubscriptionEntity, UUID>
