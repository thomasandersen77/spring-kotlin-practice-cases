/**
 * KOTLIN IDIOMS DRILL
 *
 * Koden under fungerer og testene er grønne — men den er skrevet som Java. Refaktorer til
 * idiomatisk Kotlin uten å endre oppførsel. Se README for TODO-er.
 */
data class Consultant(
	val name: String,
	val city: String?,
	val hourlyRate: Int,
	val yearsOfExperience: Int,
	val skills: List<String>,
)

class ConsultantReports {

	// TODO: refaktorer til filter/sortedBy/map-kjede uten mutable liste
	fun namesOfSeniorsSortedByRate(consultants: List<Consultant>): List<String> {
		val result = ArrayList<String>()
		val seniors = ArrayList<Consultant>()
		for (c in consultants) {
			if (c.yearsOfExperience >= 8) {
				seniors.add(c)
			}
		}
		seniors.sortWith(Comparator { a, b -> b.hourlyRate - a.hourlyRate })
		for (c in seniors) {
			result.add(c.name)
		}
		return result
	}

	// TODO: refaktorer null-håndteringen med ?. og ?:
	fun describeLocation(consultant: Consultant?): String {
		if (consultant != null) {
			if (consultant.city != null) {
				if (consultant.city.isNotBlank()) {
					return consultant.name + " jobber fra " + consultant.city
				} else {
					return consultant.name + " har ukjent lokasjon"
				}
			} else {
				return consultant.name + " har ukjent lokasjon"
			}
		}
		return "ingen konsulent"
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

	// TODO: refaktorer til sumOf
	fun totalDailyCost(consultants: List<Consultant>, hoursPerDay: Int): Int {
		var total = 0
		for (c in consultants) {
			total = total + c.hourlyRate * hoursPerDay
		}
		return total
	}

	// TODO: refaktorer til when-uttrykk med expression body
	fun seniorityLabel(yearsOfExperience: Int): String {
		if (yearsOfExperience < 0) {
			throw IllegalArgumentException("Erfaring kan ikke være negativ")
		}
		if (yearsOfExperience < 3) {
			return "junior"
		} else if (yearsOfExperience < 8) {
			return "erfaren"
		} else if (yearsOfExperience < 15) {
			return "senior"
		} else {
			return "veteran"
		}
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
