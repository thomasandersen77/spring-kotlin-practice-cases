/**
 * KOTLIN IDIOMS DRILL
 *
 * Koden under fungerer og testene er grønne — men den er skrevet som Java.
 * Refaktorer til idiomatisk Kotlin uten å endre oppførsel. Se README for TODO-er.
 */

data class Consultant(
    val name: String,
    val city: String?,
    val hourlyRate: Int,
    val yearsOfExperience: Int,
    val skills: List<String>
)

class ConsultantReports {

    fun namesOfSeniorsSortedByRate(consultants: List<Consultant>): List<String> =
        consultants
            .filter { it.yearsOfExperience >= 8 }
            .sortedByDescending { it.hourlyRate }
            .map { it.name }

    fun describeLocation(consultant: Consultant?): String {
        consultant ?: return "ingen konsulent"
        val city = consultant.city?.takeIf { it.isNotBlank() }
            ?: return "${consultant.name} har ukjent lokasjon"

        return "${consultant.name} jobber fra $city"
    }

    fun buildSkillIndex(consultants: List<Consultant>): Map<String, List<String>> =
        consultants
            .flatMap { consultant ->
                consultant.skills.map { skill -> skill to consultant.name }
            }
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })

    fun totalDailyCost(consultants: List<Consultant>, hoursPerDay: Int): Int =
        consultants.sumOf { it.hourlyRate * hoursPerDay }

    fun seniorityLabel(yearsOfExperience: Int): String = when {
        yearsOfExperience < 0 -> throw IllegalArgumentException("Erfaring kan ikke være negativ")
        yearsOfExperience < 3 -> "junior"
        yearsOfExperience < 8 -> "erfaren"
        yearsOfExperience < 15 -> "senior"
        else -> "veteran"
    }

    fun summaryLine(consultants: List<Consultant>, city: String): String =
        consultants.availableIn(city)
            .joinToString(prefix = "Konsulenter i $city: ") { it.name }
}

fun List<Consultant>.availableIn(city: String): List<Consultant> =
    filter { consultant -> consultant.city == city }
