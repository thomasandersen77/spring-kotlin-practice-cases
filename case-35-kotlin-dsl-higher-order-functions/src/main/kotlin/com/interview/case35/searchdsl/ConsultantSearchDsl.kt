package com.interview.case35.searchdsl

import kotlin.math.roundToInt

/**
 * DSL, HIGHER-ORDER FUNCTIONS OG DELEGERING
 *
 * Målet er et lesbart søke-API:
 *
 *     val treff = consultants.search {
 *         skill("Kotlin")
 *         availableOnly()
 *         anyOf {
 *             inCity("Oslo")
 *             maxHourlyRate(1200)
 *         }
 *     }
 *
 * Alt som skal implementeres er markert med `TODO()`. Testene beskriver kontrakten.
 * Se README for TODO-liste, læringsmål og akseptansekriterier.
 */

data class Consultant(
    val name: String,
    val city: String,
    val hourlyRateNok: Int,
    val yearsOfExperience: Int,
    val skills: Set<String>,
    val available: Boolean
)

/** Et søkekriterium er bare en funksjon. Det er hele poenget med higher-order functions. */
typealias ConsultantPredicate = (Consultant) -> Boolean

@DslMarker
annotation class ConsultantSearchDsl

/**
 * Builder for søke-DSL-en. Kriterier på toppnivå kombineres med AND.
 *
 * TODO 1: Velg intern representasjon (f.eks. en privat liste av `ConsultantPredicate`)
 * og implementer kriteriefunksjonene under. Ingenting av dette skal være synlig utenfra.
 */
@ConsultantSearchDsl
class ConsultantSearchBuilder {

    private val predicates = mutableListOf<ConsultantPredicate>()

    /** TODO 2: Konsulenten må ha denne ferdigheten (eksakt match). */
    fun skill(name: String) {
        predicates += { consultant -> name in consultant.skills }
    }

    /** TODO 3: Konsulenten må være i denne byen (eksakt match). */
    fun inCity(city: String) {
        predicates += { consultant -> consultant.city == city }
    }

    /** TODO 4: Timepris mindre enn eller lik `rateNok`. */
    fun maxHourlyRate(rateNok: Int) {
        predicates += { consultant -> consultant.hourlyRateNok <= rateNok }
    }

    /** TODO 5: Minst så mange års erfaring. */
    fun minYearsOfExperience(years: Int) {
        predicates += { consultant -> consultant.yearsOfExperience >= years }
    }

    /** TODO 6: Bare tilgjengelige konsulenter. */
    fun availableOnly() {
        predicates += Consultant::available
    }

    /**
     * TODO 7: OR-gruppe. Kriteriene inne i blokken kombineres med OR, og gruppen som helhet
     * kombineres med AND mot resten av søket. En tom `anyOf`-blokk skal matche alle.
     * Hint: bygg en ny `ConsultantSearchBuilder`, kjør blokken på den, og kombiner predikatene.
     */
    fun anyOf(block: ConsultantSearchBuilder.() -> Unit) {
        val anyOfPredicate = ConsultantSearchBuilder()
            .apply(block)
            .buildAny()
        predicates += anyOfPredicate
    }

    /**
     * TODO 8: Bygg det samlede predikatet. Ingen kriterier = matcher alle.
     * Hint: `all { }`/`none { }` over de innsamlede predikatene, eller `fold`.
     */
    fun build(): ConsultantPredicate =
        { consultant -> predicates.all { predicate -> predicate(consultant) } }

    private fun buildAny(): ConsultantPredicate =
        { consultant -> predicates.isEmpty() || predicates.any { predicate -> predicate(consultant) } }
}

/**
 * TODO 9: Inngangspunkt til DSL-en: kjør blokken på en ny builder og returner predikatet.
 */
fun consultantSearch(block: ConsultantSearchBuilder.() -> Unit): ConsultantPredicate =
    ConsultantSearchBuilder().apply(block).build()

/**
 * TODO 10: Søk direkte på en liste. Rekkefølgen fra kildelisten bevares.
 */
fun List<Consultant>.search(block: ConsultantSearchBuilder.() -> Unit): List<Consultant> =
    filter(consultantSearch(block))

/** TODO 11: Kombiner to predikater med AND. */
infix fun ConsultantPredicate.and(other: ConsultantPredicate): ConsultantPredicate =
    { consultant -> this(consultant) && other(consultant) }

/** TODO 12: Kombiner to predikater med OR. */
infix fun ConsultantPredicate.or(other: ConsultantPredicate): ConsultantPredicate =
    { consultant -> this(consultant) || other(consultant) }

/** TODO 13: Inverter et predikat, slik at `!predikat` fungerer. */
operator fun ConsultantPredicate.not(): ConsultantPredicate =
    { consultant -> !this(consultant) }

/**
 * TODO 14: Higher-order function som lager et predikat: konsulenten må ha ALLE ferdighetene.
 * Ingen ferdigheter oppgitt = matcher alle.
 */
fun hasAllSkills(vararg skills: String): ConsultantPredicate =
    { consultant -> skills.all(consultant.skills::contains) }

/**
 * Indeks over konsulenter. `load` er dyr (tenk databasekall) og skal kalles maks én gang —
 * og bare hvis indeksen faktisk brukes.
 *
 * TODO 15: Bytt ut `get() = TODO(...)` med delegert lazy-initialisering (`by lazy`), slik at
 * `load` kalles én gang totalt, uansett hvor mange av medlemmene som brukes.
 */
class ConsultantIndex(private val load: () -> List<Consultant>) {

    private val consultants: List<Consultant> by lazy(load)

    /** Ferdighet -> konsulenter, i samme rekkefølge som kildelisten. */
    val bySkill: Map<String, List<Consultant>> by lazy {
        consultants
            .flatMap { consultant ->
                consultant.skills.map { skill -> skill to consultant }
            }
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })
    }

    /** By -> konsulenter, i samme rekkefølge som kildelisten. */
    val byCity: Map<String, List<Consultant>> by lazy {
        consultants.groupBy(Consultant::city)
    }

    /** Tom liste for ukjent ferdighet. */
    fun withSkill(skill: String): List<Consultant> =
        bySkill[skill].orEmpty()
}

/**
 * TODO 16: Bygg en rapport med `buildString`.
 * Kontrakt (linjer skilt med "\n", ingen linjeskift på slutten):
 * - første linje: "Rapport: <title>"
 * - deretter én linje per konsulent, sortert på navn asc:
 *   "- <name> (<city>), <hourlyRateNok> kr/t, <yearsOfExperience> år"
 * - siste linje: "Snittpris: <gjennomsnitt avrundet til nærmeste hele> kr/t"
 * - tom liste: "Rapport: <title>" etterfulgt av linjen "(ingen treff)" og ingen snittpris
 */
fun List<Consultant>.toReport(title: String): String = buildString {
    append("Rapport: $title")

    if (this@toReport.isEmpty()) {
        append("\n(ingen treff)")
    } else {
        this@toReport
            .sortedBy(Consultant::name)
            .forEach { consultant ->
                append(
                    "\n- ${consultant.name} (${consultant.city}), " +
                        "${consultant.hourlyRateNok} kr/t, ${consultant.yearsOfExperience} år"
                )
            }

        val averageRate = this@toReport
            .map(Consultant::hourlyRateNok)
            .average()
            .roundToInt()
        append("\nSnittpris: $averageRate kr/t")
    }
}
