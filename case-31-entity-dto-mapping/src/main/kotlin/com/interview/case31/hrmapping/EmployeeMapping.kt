package com.interview.case31.hrmapping

import com.interview.case31.hrmapping.Audience.*
import java.time.LocalDate

/**
 * MAPPING: ENTITET -> DTO
 *
 * Entitetene under er "persistensmodellen": mange felter, noen nullable, noen sensitive.
 * DTO-ene er API-modellen: færre felter, andre navn, og innhold som avhenger av hvem som spør.
 *
 * Alle mapping-funksjonene er uløste (`TODO()`). Testene beskriver kontrakten.
 * Se README for TODO-liste, læringsmål og akseptansekriterier.
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
 * TODO 1: Implementer som extension property.
 * Kontrakt: "$firstName $lastName".
 */
val EmployeeEntity.fullName: String
    get() = "$firstName $lastName"

/**
 * TODO 2: Implementer som extension function med expression body.
 * Kontrakt: ansatt er aktiv når employmentStart <= date OG (employmentEnd == null ELLER employmentEnd >= date).
 */
fun EmployeeEntity.isActiveOn(date: LocalDate): Boolean =
    employmentStart <= date &&
            (employmentEnd == null || employmentEnd >= date)

/**
 * TODO 3: Map én entitet til DTO. Ikke alle felter skal alltid med:
 * - `nationalIdentityNumber` og `internalNotes` skal ALDRI ut i DTO-en
 * - `email` er kun med for INTERNAL og MANAGER (null for PUBLIC)
 * - `monthlySalaryNok` er kun med for MANAGER (null ellers)
 * - `certifiedSkills` = navn på sertifiserte skills, sortert på yearsOfExperience desc, deretter navn asc
 */
fun EmployeeEntity.toDto(audience: Audience, today: LocalDate): EmployeeDto = when (audience) {
    PUBLIC -> {
        EmployeeDto(
            id = id,
            fullName = fullName,
            departmentName = department.name,
            email = null,
            monthlySalaryNok = null,
            active = isActiveOn(today),
            certifiedSkills = emptyList()
        )
    }

    INTERNAL -> {
        EmployeeDto(
            id = id,
            fullName = fullName,
            departmentName = department.name,
            email = email,
            monthlySalaryNok = null,
            active = isActiveOn(today),
            certifiedSkills = filterSkillsByExperienceThenSkillName()
        )
    }

    MANAGER -> {
        EmployeeDto(
            id = id,
            fullName = fullName,
            departmentName = department.name,
            email = email,
            monthlySalaryNok = monthlySalaryNok,
            active = isActiveOn(today),
            certifiedSkills = filterSkillsByExperienceThenSkillName()
        )
    }
}

private fun EmployeeEntity.filterSkillsByExperienceThenSkillName(): List<String> {
    val skills = this.skills
        .filter { it.certified }
        .sortedWith(
            compareByDescending<SkillEntity> { skill ->
                skill.yearsOfExperience
            }.thenBy { skill ->
                skill.name
            }
        ).map { it.name }
    return skills
}


//TODO("Map entitet til DTO med audience-styrt feltutvalg")

/**
 * TODO 4: Map en liste av entiteter til en liste av DTO-er — uten mutable liste og uten for-løkke.
 */
fun List<EmployeeEntity>.toDtos(audience: Audience, today: LocalDate): List<EmployeeDto> {
    return map { it.toDto(audience, today) }
}


/**
 * TODO 5: Map OG filtrer i én operasjon: bare ansatte som er aktive på `today` skal med.
 * Hint: dette er forskjellen på `map`, `filter` + `map`, og `mapNotNull` — vær klar til å begrunne valget.
 */
fun List<EmployeeEntity>.toActiveDtos(audience: Audience, today: LocalDate): List<EmployeeDto> =
        filter { it.isActiveOn(today) }
        .map { it.toDto(audience, today) }

/**
 * TODO 6: Aggregert mapping: grupper ansatte per avdelingsnavn og bygg sammendrag.
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
): List<DepartmentSummaryDto> =
    groupBy { employee ->
        employee.department.name
    }
        .entries
        .sortedBy { (departmentName, _) ->
            departmentName
        }
        .map { (departmentName, employees) ->
            val topCertifiedSkills = employees
                .flatMap { employee ->
                    employee.skills
                }
                .filter { skill ->
                    skill.certified
                }
                .groupingBy { skill ->
                    skill.name
                }
                .eachCount()
                .entries
                .sortedWith(
                    compareByDescending<Map.Entry<String, Int>> { entry ->
                        entry.value
                    }.thenBy { entry ->
                        entry.key
                    }
                )
                .take(topSkillCount)
                .map { entry ->
                    entry.key
                }

            DepartmentSummaryDto(
                departmentName = departmentName,
                employeeCount = employees.size,
                activeEmployeeCount = employees.count { employee ->
                    employee.isActiveOn(today)
                },
                topCertifiedSkills = topCertifiedSkills
            )
        }
