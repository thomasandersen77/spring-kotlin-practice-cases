package com.interview.case37.consultants

data class ConsultantInput(val id: String, val name: String?, val yearsOfExperience: Int, val availabilityCode: String?, val partialPercent: Int?, val skills: List<SkillInput>?)
data class SkillInput(val name: String, val certified: Boolean)

sealed interface Availability {
    data object Available : Availability
    data class PartiallyAvailable(val percent: Int) : Availability
    data object Unavailable : Availability
    data class Unknown(val sourceCode: String?) : Availability
}

data class Consultant(val id: String, val name: String, val yearsOfExperience: Int, val availability: Availability, val certifiedSkills: List<String>)
data class ConsultantDto(val id: String, val displayName: String, val experienceYears: Int, val availabilityLabel: String, val certifiedSkills: List<String>)
data class SkillPopularityDto(val skillName: String, val consultantCount: Int)

// TODO 1: Oversett statuskode til sealed domenetype. PARTIAL krever en gyldig prosent.
fun availabilityFrom(code: String?, partialPercent: Int? = null): Availability =
    TODO("Oversett ekstern status til Availability")

// TODO 2: Returner null for blankt navn eller negative erfaringsår.
fun ConsultantInput.toDomain(): Consultant? =
    TODO("Map gyldig input til domene og behold bare sertifiserte skills")

// TODO 3: Map til DTO med uttømmende when for availabilityLabel.
fun Consultant.toDto(): ConsultantDto = TODO("Map domene til DTO")

// TODO 4: Map bare gyldige inputrader uten mutable liste.
fun List<ConsultantInput>.toDomainConsultants(): List<Consultant> = TODO("Bruk mapNotNull")

// TODO 5: Ta med Available og PartiallyAvailable; sorter erfaring desc og navn asc.
fun List<Consultant>.rankAvailable(): List<ConsultantDto> = TODO("Filtrer, sortedWith, map")

// TODO 6: Tell sertifiserte skill-navn; sorter antall desc og navn asc.
fun List<Consultant>.skillPopularity(): List<SkillPopularityDto> = TODO("Bruk flatMap og groupingBy")
