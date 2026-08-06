package com.interview.case37.consultants

data class ConsultantInput(val id: String, val name: String?, val yearsOfExperience: Int, val availabilityCode: String?, val partialPercent: Int?, val skills: List<SkillInput>?)
data class SkillInput(val name: String, val certified: Boolean)

sealed interface Availability {
    data object Available : Availability
    data class PartiallyAvailable(val percent: Int) : Availability {
        init {
            require(percent in 1..99) { "Partial availability must be between 1 and 99 percent" }
        }
    }
    data object Unavailable : Availability
    data class Unknown(val sourceCode: String?) : Availability
}

data class Consultant(val id: String, val name: String, val yearsOfExperience: Int, val availability: Availability, val certifiedSkills: List<String>)
data class ConsultantDto(val id: String, val displayName: String, val experienceYears: Int, val availabilityLabel: String, val certifiedSkills: List<String>)
data class SkillPopularityDto(val skillName: String, val consultantCount: Int)

// TODO 1: Oversett statuskode til sealed domenetype. PARTIAL krever en gyldig prosent.
fun availabilityFrom(code: String?, partialPercent: Int? = null): Availability =
    when (code) {
        "AVAILABLE" -> Availability.Available
        "PARTIAL" -> partialPercent
            ?.takeIf { it in 1..99 }
            ?.let(Availability::PartiallyAvailable)
            ?: Availability.Unknown(code)
        "UNAVAILABLE" -> Availability.Unavailable
        else -> Availability.Unknown(code)
    }

// TODO 2: Returner null for blankt navn eller negative erfaringsår.
fun ConsultantInput.toDomain(): Consultant? {
    val normalizedName = name?.trim()?.takeIf(String::isNotEmpty) ?: return null
    if (yearsOfExperience < 0) return null

    return Consultant(
        id = id,
        name = normalizedName,
        yearsOfExperience = yearsOfExperience,
        availability = availabilityFrom(availabilityCode, partialPercent),
        certifiedSkills = skills.orEmpty()
            .filter(SkillInput::certified)
            .map(SkillInput::name)
    )
}

// TODO 3: Map til DTO med uttømmende when for availabilityLabel.
fun Consultant.toDto(): ConsultantDto = ConsultantDto(
    id = id,
    displayName = name,
    experienceYears = yearsOfExperience,
    availabilityLabel = when (val currentAvailability = availability) {
        Availability.Available -> "Tilgjengelig"
        is Availability.PartiallyAvailable ->
            "Delvis tilgjengelig (${currentAvailability.percent} %)"
        Availability.Unavailable -> "Ikke tilgjengelig"
        is Availability.Unknown -> currentAvailability.sourceCode
            ?.let { sourceCode -> "Ukjent ($sourceCode)" }
            ?: "Ukjent"
    },
    certifiedSkills = certifiedSkills
)

// TODO 4: Map bare gyldige inputrader uten mutable liste.
fun List<ConsultantInput>.toDomainConsultants(): List<Consultant> = mapNotNull(ConsultantInput::toDomain)

// TODO 5: Ta med Available og PartiallyAvailable; sorter erfaring desc og navn asc.
fun List<Consultant>.rankAvailable(): List<ConsultantDto> =
    filter { consultant ->
        consultant.availability is Availability.Available ||
            consultant.availability is Availability.PartiallyAvailable
    }
        .sortedWith(
            compareByDescending<Consultant>(Consultant::yearsOfExperience)
                .thenBy(Consultant::name)
        )
        .map(Consultant::toDto)

// TODO 6: Tell sertifiserte skill-navn; sorter antall desc og navn asc.
fun List<Consultant>.skillPopularity(): List<SkillPopularityDto> =
    flatMap { consultant -> consultant.certifiedSkills.distinct() }
        .groupingBy { skillName -> skillName }
        .eachCount()
        .map { (skillName, consultantCount) ->
            SkillPopularityDto(skillName, consultantCount)
        }
        .sortedWith(
            compareByDescending<SkillPopularityDto>(SkillPopularityDto::consultantCount)
                .thenBy(SkillPopularityDto::skillName)
        )
