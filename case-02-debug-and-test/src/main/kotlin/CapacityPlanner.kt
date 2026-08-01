import org.springframework.web.bind.annotation.*
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
     * BUG:
     *  - Denne har minst én off-by-one-feil.
     *  - Den håndterer helg litt naivt.
     *
     * CASE-FOKUS:
     *  - Skriv kontrakt-tester før du endrer implementasjonen.
     *  - Avklar om `to` er inclusive eller exclusive, og la testnavn dokumentere valget.
     *  - Del gjerne opp i navngitte hjelpefunksjoner som uttrykker domenespråk (arbeidsdag, fravær, periode).
     *  - Forklar trade-off mellom enkelhet og tydelighet i dato-semantikken.
     */
    fun availableWorkingDays(
        from: LocalDate,
        to: LocalDate,
        absenceDates: Set<LocalDate>
    ): Int {

        var date = from
        var count = 0

        while (date.isBefore(to)) {
            val isWeekend = date.isInWeekend()
            val isAbsent = absenceDates.contains(date) && !isWeekend

            if (!isWeekend && !isAbsent) {
                count++
            }

            date = date.plusDays(1)
        }

        return count
    }
}

fun LocalDate.isInWeekend(): Boolean =
    dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY

