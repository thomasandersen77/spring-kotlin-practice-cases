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
        if (consultant == null) return "ingen konsulent"

        val city = consultant.city

        return if (city.isNullOrBlank()) {
            "${consultant.name} har ukjent lokasjon"
        } else {
            "${consultant.name} jobber fra $city"
        }

        /*
         * En kjedet løsning med takeIf, let og Elvis kan være mer kompakt,
         * men den eksplisitte kontrollflyten er enklere å lese.
         * Scope-funksjoner brukes når de faktisk forbedrer lesbarheten.
         */
    }

    // TODO: refaktorer til groupBy + mapValues som én expression-body-funksjon
    fun buildSkillIndex(consultants: List<Consultant>): Map<String, List<String>> {
        val index = HashMap<String, MutableList<String>>()
        for (c in consultants) {
            for (skill in c.skills) {
                if (!index.containsKey(skill)) {
                    index[skill] = ArrayList()
                }
                index[skill]!!.add(c.name)
            }
        }
        return index
    }

    fun totalDailyCost(
        consultants: List<Consultant>,
        hoursPerDay: Int
    ): Int = consultants.sumOf { it.hourlyRate * hoursPerDay }

    fun seniorityLabel(yearsOfExperience: Int): String =
        when {
            yearsOfExperience in 0..2 -> "junior"
            yearsOfExperience in 3..7 -> "erfaren"
            yearsOfExperience in 8..14 -> "senior"
            yearsOfExperience >= 15 -> "veteran"
            else -> throw IllegalArgumentException("Erfaring kan ikke være negativ")
        }

    // TODO: refaktorer til joinToString, og vurder en extension function for filtreringen
    fun summaryLine(consultants: List<Consultant>, city: String): String {
        val inCity = ArrayList<Consultant>()
        for (c in consultants) {
            if (c.city != null && c.city == city) {
                inCity.add(c)
            }
        }
        var line = "Konsulenter i " + city + ": "
        var first = true
        for (c in inCity) {
            if (!first) {
                line = line + ", "
            }
            line = line + c.name
            first = false
        }
        return line
    }
}
