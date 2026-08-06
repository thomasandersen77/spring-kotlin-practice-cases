package com.interview.case31.hrmapping

import java.time.LocalDate

/**
 * MAPPING: ENTITET -> DTO
 *
 * Entitetene under er "persistensmodellen": mange felter, noen nullable, noen sensitive.
 * DTO-ene er API-modellen: færre felter, andre navn, og innhold som avhenger av hvem som spør.
 *
 * Mapping-funksjonene under implementerer kontrakten som beskrives i README og testene.
 */

// ---------- Entiteter (persistenslaget) ----------

data class SkillEntity(
    val name: String,
    val yearsOfExperience: Int,
    val certified: Boolean
)

data class DepartmentEntity(
    val id: Long,
    val name: String,
    val costCenter: String
)

data class EmployeeEntity(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val nationalIdentityNumber: String,
    val email: String?,
    val phone: String?,
    val monthlySalaryNok: Int,
    val department: DepartmentEntity,
    val employmentStart: LocalDate,
    val employmentEnd: LocalDate?,
    val skills: List<SkillEntity>,
    val internalNotes: String?
)

// ---------- API-modell (DTO-er) ----------

/** Hvem spør? Bestemmer hvilke felter som er lov å eksponere. */
enum class Audience {
    PUBLIC,
    INTERNAL,
    MANAGER
}

data class EmployeeDto(
    val id: Long,
    val fullName: String,
    val departmentName: String,
    val email: String?,
    val monthlySalaryNok: Int?,
    val active: Boolean,
    val certifiedSkills: List<String>
)

data class DepartmentSummaryDto(
    val departmentName: String,
    val employeeCount: Int,
    val activeEmployeeCount: Int,
    val topCertifiedSkills: List<String>
)

// ---------- Mapping (din jobb) ----------

/**
 * Regel 1: Implementert som extension property.
 * Kontrakt: "$firstName $lastName".
 */
val EmployeeEntity.fullName: String
    get() = "$firstName $lastName"

/**
 * Regel 2: Implementert som extension function med expression body.
 * Kontrakt: ansatt er aktiv når employmentStart <= date OG (employmentEnd == null ELLER employmentEnd >= date).
 */
fun EmployeeEntity.isActiveOn(date: LocalDate): Boolean =
    !employmentStart.isAfter(date) && (employmentEnd == null || !employmentEnd.isBefore(date))

/**
 * Regel 3: Map én entitet til DTO. Ikke alle felter skal alltid med:
 * - `nationalIdentityNumber` og `internalNotes` skal ALDRI ut i DTO-en
 * - `email` er kun med for INTERNAL og MANAGER (null for PUBLIC)
 * - `monthlySalaryNok` er kun med for MANAGER (null ellers)
 * - `certifiedSkills` = navn på sertifiserte skills, sortert på yearsOfExperience desc, deretter navn asc
 */
fun EmployeeEntity.toDto(audience: Audience, today: LocalDate): EmployeeDto =
    EmployeeDto(
        id = id,
        fullName = fullName,
        departmentName = department.name,
        email = email.takeIf { audience.canSeeEmail },
        monthlySalaryNok = monthlySalaryNok.takeIf { audience.canSeeSalary },
        active = isActiveOn(today),
        certifiedSkills = skills
            .filter(SkillEntity::certified)
            .sortedWith(
                compareByDescending<SkillEntity> { it.yearsOfExperience }
                    .thenBy { it.name }
            )
            .map(SkillEntity::name)
    )

/**
 * Regel 4: Map en liste av entiteter til en liste av DTO-er — uten mutable liste og uten for-løkke.
 */
fun List<EmployeeEntity>.toDtos(audience: Audience, today: LocalDate): List<EmployeeDto> =
    map { employee -> employee.toDto(audience, today) }

/**
 * Regel 5: Map og filtrer: bare ansatte som er aktive på `today` skal med.
 * Hint: dette er forskjellen på `map`, `filter` + `map`, og `mapNotNull` — vær klar til å begrunne valget.
 */
fun List<EmployeeEntity>.toActiveDtos(audience: Audience, today: LocalDate): List<EmployeeDto> =
    filter { employee -> employee.isActiveOn(today) }
        .map { employee -> employee.toDto(audience, today) }

/**
 * Regel 6: Aggregert mapping: grupper ansatte per avdelingsnavn og bygg sammendrag.
 * Kontrakt:
 * - én rad per avdelingsnavn, sortert på avdelingsnavn asc
 * - employeeCount = alle ansatte i avdelingen
 * - activeEmployeeCount = de som er aktive på `today`
 * - topCertifiedSkills = de `topSkillCount` mest utbredte sertifiserte skill-navnene i avdelingen,
 *   sortert på antall desc, deretter navn asc
 */
fun List<EmployeeEntity>.toDepartmentSummaries(
    today: LocalDate,
    topSkillCount: Int = 3
): List<DepartmentSummaryDto> {
    require(topSkillCount >= 0) { "topSkillCount cannot be negative" }

    return groupBy { employee -> employee.department.name }
        .map { (departmentName, employees) ->
            DepartmentSummaryDto(
                departmentName = departmentName,
                employeeCount = employees.size,
                activeEmployeeCount = employees.count { it.isActiveOn(today) },
                topCertifiedSkills = employees.topCertifiedSkills(topSkillCount)
            )
        }
        .sortedBy(DepartmentSummaryDto::departmentName)
}

private val Audience.canSeeEmail: Boolean
    get() = this != Audience.PUBLIC

private val Audience.canSeeSalary: Boolean
    get() = this == Audience.MANAGER

private fun List<EmployeeEntity>.topCertifiedSkills(limit: Int): List<String> =
    flatMap(EmployeeEntity::skills)
        .filter(SkillEntity::certified)
        .groupingBy(SkillEntity::name)
        .eachCount()
        .entries
        .sortedWith(
            compareByDescending<Map.Entry<String, Int>> { it.value }
                .thenBy { it.key }
        )
        .take(limit)
        .map(Map.Entry<String, Int>::key)
