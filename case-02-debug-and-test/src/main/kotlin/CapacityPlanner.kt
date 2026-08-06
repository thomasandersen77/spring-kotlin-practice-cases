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
    fun availableWorkingDays(
        from: LocalDate,
        to: LocalDate,
        absenceDates: Set<LocalDate>
    ): Int {
        require(!from.isAfter(to)) { "from must be on or before to" }

        return datesInHalfOpenPeriod(from, to)
            .count { date -> date.isWorkingDay() && date !in absenceDates }
    }

    private fun datesInHalfOpenPeriod(from: LocalDate, to: LocalDate): Sequence<LocalDate> =
        generateSequence(from) { date -> date.plusDays(1) }
            .takeWhile { date -> date.isBefore(to) }

    private fun LocalDate.isWorkingDay(): Boolean =
        dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY
}
