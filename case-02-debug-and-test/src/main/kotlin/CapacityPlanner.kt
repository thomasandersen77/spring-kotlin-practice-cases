import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.DayOfWeek
import java.time.LocalDate

@RestController
@RequestMapping("/capacity")
class CapacityController(
    private val planner: CapacityPlanner = CapacityPlanner()
) {
    @PostMapping("/available-days")
    fun availableDays(@RequestBody request: CapacityRequest): CapacityResponse {
        val days = planner.availableWorkingDays(request.from, request.to, request.absenceDates)
        return CapacityResponse(days)
    }
}

data class CapacityRequest(
    val from: LocalDate,
    val to: LocalDate,
    val absenceDates: Set<LocalDate>
)

data class CapacityResponse(
    val availableDays: Int
)

class CapacityPlanner {

    /**
     * Datokontrakt:
     *  - `to` er eksklusiv: perioden er [from, to), og from == to gir 0.
     *  - `from > to` er en kallfeil og avvises med exception, ikke stille 0.
     *  - Helg er aldri en arbeidsdag; fravær på en helgedag påvirker ikke tellingen.
     */
    fun availableWorkingDays(
        from: LocalDate,
        to: LocalDate,
        absenceDates: Set<LocalDate>
    ): Int {
        require(!from.isAfter(to)) { "from ($from) kan ikke være etter to ($to)" }

        return from.datesUntil(to) // end-eksklusiv, matcher kontrakten
            .filter { date -> date.isWorkingDay(absenceDates) }
            .count()
            .toInt()
    }

    private fun LocalDate.isWorkingDay(absenceDates: Set<LocalDate>): Boolean =
        !isInWeekend() && this !in absenceDates
}

fun LocalDate.isInWeekend(): Boolean =
    dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY


