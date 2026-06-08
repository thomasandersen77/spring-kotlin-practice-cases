import org.springframework.web.bind.annotation.*
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
     * TODO:
     *  - Skriv tester før du fikser
     *  - Avklar om 'to' er inclusive eller exclusive
     *  - Refaktorer til lesbar kode
     */
    fun availableWorkingDays(
        from: LocalDate,
        to: LocalDate,
        absenceDates: Set<LocalDate>
    ): Int {
        var date = from
        var count = 0

        while (date.isBefore(to)) {
            val dayOfWeek = date.dayOfWeek.value
            val isWeekend = dayOfWeek == 6 || dayOfWeek == 7
            val isAbsent = absenceDates.contains(date)

            if (!isWeekend && !isAbsent) {
                count++
            }

            date = date.plusDays(1)
        }

        return count
    }
}
