package com.training.case35.searchdsl

/**
 * DSL, HIGHER-ORDER FUNCTIONS OG DELEGERING
 *
 * Målet er et lesbart søke-API:
 *
 * val treff = consultants.search { skill("Kotlin") availableOnly() anyOf { inCity("Oslo")
 * maxHourlyRate(1200) } }
 *
 * Alt som skal implementeres er markert med `TODO()`. Testene beskriver kontrakten. Se README for
 * TODO-liste, læringsmål og akseptansekriterier.
 */
data class Consultant(
	val name: String,
	val city: String,
	val hourlyRateNok: Int,
	val yearsOfExperience: Int,
	val skills: Set<String>,
	val available: Boolean,
)

/** Et søkekriterium er bare en funksjon. Det er hele poenget med higher-order functions. */
typealias ConsultantPredicate = (Consultant) -> Boolean

/**
 * Builder for søke-DSL-en. Kriterier på toppnivå kombineres med AND.
 *
 * TODO 1: Velg intern representasjon (f.eks. en privat liste av `ConsultantPredicate`) og
 * implementer kriteriefunksjonene under. Ingenting av dette skal være synlig utenfra.
 */
class ConsultantSearchBuilder {

	/** TODO 2: Konsulenten må ha denne ferdigheten (eksakt match). */
	fun skill(name: String) {
		TODO("Legg til ferdighetskriterium")
	}

	/** TODO 3: Konsulenten må være i denne byen (eksakt match). */
	fun inCity(city: String) {
		TODO("Legg til bykriterium")
	}

	/** TODO 4: Timepris mindre enn eller lik `rateNok`. */
	fun maxHourlyRate(rateNok: Int) {
		TODO("Legg til priskriterium")
	}

	/** TODO 5: Minst så mange års erfaring. */
	fun minYearsOfExperience(years: Int) {
		TODO("Legg til erfaringskriterium")
	}

	/** TODO 6: Bare tilgjengelige konsulenter. */
	fun availableOnly() {
		TODO("Legg til tilgjengelighetskriterium")
	}

	/**
	 * TODO 7: OR-gruppe. Kriteriene inne i blokken kombineres med OR, og gruppen som helhet
	 * kombineres med AND mot resten av søket. En tom `anyOf`-blokk skal matche alle. Hint: bygg en
	 * ny `ConsultantSearchBuilder`, kjør blokken på den, og kombiner predikatene.
	 */
	fun anyOf(block: ConsultantSearchBuilder.() -> Unit) {
		TODO("Implementer OR-gruppe med lambda with receiver")
	}

	/**
	 * TODO 8: Bygg det samlede predikatet. Ingen kriterier = matcher alle. Hint: `all { }`/`none {
	 * }` over de innsamlede predikatene, eller `fold`.
	 */
	fun build(): ConsultantPredicate = TODO("Bygg samlet predikat")
}

/** TODO 9: Inngangspunkt til DSL-en: kjør blokken på en ny builder og returner predikatet. */
fun consultantSearch(block: ConsultantSearchBuilder.() -> Unit): ConsultantPredicate =
	TODO("Implementer DSL-inngangspunktet")

/** TODO 10: Søk direkte på en liste. Rekkefølgen fra kildelisten bevares. */
fun List<Consultant>.search(block: ConsultantSearchBuilder.() -> Unit): List<Consultant> =
	TODO("Implementer søk på liste")

/** TODO 11: Kombiner to predikater med AND. */
infix fun ConsultantPredicate.and(other: ConsultantPredicate): ConsultantPredicate =
	TODO("Implementer AND-komposisjon")

/** TODO 12: Kombiner to predikater med OR. */
infix fun ConsultantPredicate.or(other: ConsultantPredicate): ConsultantPredicate =
	TODO("Implementer OR-komposisjon")

/** TODO 13: Inverter et predikat, slik at `!predikat` fungerer. */
operator fun ConsultantPredicate.not(): ConsultantPredicate =
	TODO("Implementer negasjon som operator")

/**
 * TODO 14: Higher-order function som lager et predikat: konsulenten må ha ALLE ferdighetene. Ingen
 * ferdigheter oppgitt = matcher alle.
 */
fun hasAllSkills(vararg skills: String): ConsultantPredicate =
	TODO("Implementer predikatfabrikk med vararg")

/**
 * Indeks over konsulenter. `load` er dyr (tenk databasekall) og skal kalles maks én gang — og bare
 * hvis indeksen faktisk brukes.
 *
 * TODO 15: Bytt ut `get() = TODO(...)` med delegert lazy-initialisering (`by lazy`), slik at `load`
 * kalles én gang totalt, uansett hvor mange av medlemmene som brukes.
 */
class ConsultantIndex(private val load: () -> List<Consultant>) {

	/** Ferdighet -> konsulenter, i samme rekkefølge som kildelisten. */
	val bySkill: Map<String, List<Consultant>>
		get() = TODO("Implementer lazy ferdighetsindeks")

	/** By -> konsulenter, i samme rekkefølge som kildelisten. */
	val byCity: Map<String, List<Consultant>>
		get() = TODO("Implementer lazy byindeks")

	/** Tom liste for ukjent ferdighet. */
	fun withSkill(skill: String): List<Consultant> = TODO("Slå opp i ferdighetsindeksen")
}

/**
 * TODO 16: Bygg en rapport med `buildString`. Kontrakt (linjer skilt med "\n", ingen linjeskift på
 * slutten):
 * - første linje: "Rapport: <title>"
 * - deretter én linje per konsulent, sortert på navn asc: "- <name> (<city>), <hourlyRateNok> kr/t,
 *   <yearsOfExperience> år"
 * - siste linje: "Snittpris: <gjennomsnitt avrundet til nærmeste hele> kr/t"
 * - tom liste: "Rapport: <title>" etterfulgt av linjen "(ingen treff)" og ingen snittpris
 */
fun List<Consultant>.toReport(title: String): String =
	TODO("Implementer rapportbygging med buildString")
