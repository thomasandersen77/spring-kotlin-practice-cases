package com.training.case31.hrmapping

import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EmployeeMappingTest {

	private val today: LocalDate = LocalDate.of(2026, 8, 3)

	private val utvikling = DepartmentEntity(id = 10, name = "Utvikling", costCenter = "CC-100")
	private val arkitektur = DepartmentEntity(id = 20, name = "Arkitektur", costCenter = "CC-200")

	private val kari =
		EmployeeEntity(
			id = 1,
			firstName = "Kari",
			lastName = "Nordmann",
			nationalIdentityNumber = "01019012345",
			email = "kari@firma.no",
			phone = "99887766",
			monthlySalaryNok = 78_000,
			department = utvikling,
			employmentStart = LocalDate.of(2018, 1, 1),
			employmentEnd = null,
			skills =
				listOf(
					SkillEntity("Kotlin", yearsOfExperience = 10, certified = true),
					SkillEntity("Spring Boot", yearsOfExperience = 6, certified = true),
					SkillEntity("AWS", yearsOfExperience = 6, certified = true),
					SkillEntity("Docker", yearsOfExperience = 3, certified = false),
				),
			internalNotes = "Ønsker ny rolle i 2027",
		)

	private val ola =
		EmployeeEntity(
			id = 2,
			firstName = "Ola",
			lastName = "Hansen",
			nationalIdentityNumber = "02029012345",
			email = null,
			phone = null,
			monthlySalaryNok = 65_000,
			department = utvikling,
			employmentStart = LocalDate.of(2020, 3, 1),
			employmentEnd = LocalDate.of(2026, 6, 30),
			skills =
				listOf(
					SkillEntity("Kotlin", yearsOfExperience = 4, certified = true),
					SkillEntity("React", yearsOfExperience = 5, certified = false),
				),
			internalNotes = null,
		)

	private val liv =
		EmployeeEntity(
			id = 3,
			firstName = "Liv",
			lastName = "Berg",
			nationalIdentityNumber = "03039012345",
			email = "liv@firma.no",
			phone = "40404040",
			monthlySalaryNok = 92_000,
			department = arkitektur,
			employmentStart = LocalDate.of(2015, 8, 1),
			employmentEnd = null,
			skills =
				listOf(
					SkillEntity("Kotlin", yearsOfExperience = 12, certified = true),
					SkillEntity("Arkitektur", yearsOfExperience = 9, certified = true),
				),
			internalNotes = "Mentor",
		)

	private val per =
		EmployeeEntity(
			id = 4,
			firstName = "Per",
			lastName = "Ås",
			nationalIdentityNumber = "04049012345",
			email = "per@firma.no",
			phone = null,
			monthlySalaryNok = 55_000,
			department = utvikling,
			employmentStart = LocalDate.of(2026, 9, 1),
			employmentEnd = null,
			skills = emptyList(),
			internalNotes = null,
		)

	private val allEmployees = listOf(kari, ola, liv, per)

	@Test
	fun `fullName kombinerer fornavn og etternavn`() {
		assertThat(kari.fullName).isEqualTo("Kari Nordmann")
		assertThat(per.fullName).isEqualTo("Per Ås")
	}

	@Test
	fun `ansatt er aktiv fra og med startdato til og med sluttdato`() {
		assertThat(kari.isActiveOn(today)).isTrue()
		assertThat(ola.isActiveOn(today)).isFalse()
		assertThat(per.isActiveOn(today)).isFalse()

		assertThat(per.copy(employmentStart = today).isActiveOn(today)).isTrue()
		assertThat(kari.copy(employmentEnd = today).isActiveOn(today)).isTrue()
		assertThat(kari.copy(employmentEnd = today.minusDays(1)).isActiveOn(today)).isFalse()
	}

	@Test
	fun `PUBLIC skjuler epost og lonn`() {
		val dto = kari.toDto(Audience.PUBLIC, today)

		assertThat(dto.id).isEqualTo(1)
		assertThat(dto.fullName).isEqualTo("Kari Nordmann")
		assertThat(dto.departmentName).isEqualTo("Utvikling")
		assertThat(dto.email).isNull()
		assertThat(dto.monthlySalaryNok).isNull()
		assertThat(dto.active).isTrue()
	}

	@Test
	fun `INTERNAL far epost men ikke lonn`() {
		val dto = kari.toDto(Audience.INTERNAL, today)

		assertThat(dto.email).isEqualTo("kari@firma.no")
		assertThat(dto.monthlySalaryNok).isNull()
	}

	@Test
	fun `MANAGER far bade epost og lonn`() {
		val dto = kari.toDto(Audience.MANAGER, today)

		assertThat(dto.email).isEqualTo("kari@firma.no")
		assertThat(dto.monthlySalaryNok).isEqualTo(78_000)
	}

	@Test
	fun `epost som mangler i entiteten blir null selv for MANAGER`() {
		assertThat(ola.toDto(Audience.MANAGER, today).email).isNull()
	}

	@Test
	fun `bare sertifiserte skills mappes, sortert pa erfaring og deretter navn`() {
		val dto = kari.toDto(Audience.INTERNAL, today)

		assertThat(dto.certifiedSkills).containsExactly("Kotlin", "AWS", "Spring Boot")
	}

	@Test
	fun `ansatt uten skills far tom liste, ikke null`() {
		assertThat(per.toDto(Audience.PUBLIC, today).certifiedSkills).isEmpty()
	}

	@Test
	fun `liste av entiteter mappes til liste av DTOer i samme rekkefolge`() {
		val dtos = allEmployees.toDtos(Audience.INTERNAL, today)

		assertThat(dtos).hasSize(4)
		assertThat(dtos.map { it.id }).containsExactly(1L, 2L, 3L, 4L)
		assertThat(dtos.map { it.fullName })
			.containsExactly("Kari Nordmann", "Ola Hansen", "Liv Berg", "Per Ås")
	}

	@Test
	fun `bare aktive ansatte mappes av toActiveDtos`() {
		val dtos = allEmployees.toActiveDtos(Audience.INTERNAL, today)

		assertThat(dtos.map { it.fullName }).containsExactly("Kari Nordmann", "Liv Berg")
		assertThat(dtos).allMatch { it.active }
	}

	@Test
	fun `avdelingssammendrag grupperer og teller aktive ansatte`() {
		val summaries = allEmployees.toDepartmentSummaries(today)

		assertThat(summaries.map { it.departmentName }).containsExactly("Arkitektur", "Utvikling")

		val arkitekturSummary = summaries.first { it.departmentName == "Arkitektur" }
		assertThat(arkitekturSummary.employeeCount).isEqualTo(1)
		assertThat(arkitekturSummary.activeEmployeeCount).isEqualTo(1)
		assertThat(arkitekturSummary.topCertifiedSkills).containsExactly("Arkitektur", "Kotlin")

		val utviklingSummary = summaries.first { it.departmentName == "Utvikling" }
		assertThat(utviklingSummary.employeeCount).isEqualTo(3)
		assertThat(utviklingSummary.activeEmployeeCount).isEqualTo(1)
		assertThat(utviklingSummary.topCertifiedSkills)
			.containsExactly("Kotlin", "AWS", "Spring Boot")
	}

	@Test
	fun `topCertifiedSkills begrenses av topSkillCount`() {
		val utviklingSummary =
			allEmployees.toDepartmentSummaries(today, topSkillCount = 1).first {
				it.departmentName == "Utvikling"
			}

		assertThat(utviklingSummary.topCertifiedSkills).containsExactly("Kotlin")
	}

	@Test
	fun `tom liste gir tomme resultater`() {
		val employees = emptyList<EmployeeEntity>()

		assertThat(employees.toDtos(Audience.MANAGER, today)).isEmpty()
		assertThat(employees.toActiveDtos(Audience.MANAGER, today)).isEmpty()
		assertThat(employees.toDepartmentSummaries(today)).isEmpty()
	}
}
