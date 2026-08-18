package com.training.case37.consultants

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConsultantMappingTest {
	private val kotlin = SkillInput("Kotlin", true)
	private val docker = SkillInput("Docker", false)

	@Test
	fun `alle statuskoder oversettes til sealed varianter`() {
		assertThat(availabilityFrom("AVAILABLE")).isEqualTo(Availability.Available)
		assertThat(availabilityFrom("PARTIAL", 60)).isEqualTo(Availability.PartiallyAvailable(60))
		assertThat(availabilityFrom("UNAVAILABLE")).isEqualTo(Availability.Unavailable)
		assertThat(availabilityFrom("NEW")).isEqualTo(Availability.Unknown("NEW"))
	}

	@Test
	fun `ugyldige rader filtreres og nullable skills blir tom liste`() {
		val inputs =
			listOf(
				ConsultantInput("1", "Kari", 10, "AVAILABLE", null, null),
				ConsultantInput("2", " ", 4, "AVAILABLE", null, listOf(kotlin)),
				ConsultantInput("3", "Ola", -1, "AVAILABLE", null, listOf(kotlin)),
			)
		assertThat(inputs.toDomainConsultants())
			.containsExactly(Consultant("1", "Kari", 10, Availability.Available, emptyList()))
	}

	@Test
	fun `mapping beholder bare sertifiserte skills`() {
		val input = ConsultantInput("1", "Kari", 10, "AVAILABLE", null, listOf(kotlin, docker))
		assertThat(input.toDomain()!!.toDto())
			.isEqualTo(ConsultantDto("1", "Kari", 10, "Tilgjengelig", listOf("Kotlin")))
	}

	@Test
	fun `kandidater sorteres pa erfaring desc og navn asc ved likhet`() {
		val values =
			listOf(
				Consultant("1", "Spring", 6, Availability.Available, emptyList()),
				Consultant("2", "AWS", 6, Availability.PartiallyAvailable(50), emptyList()),
				Consultant("3", "Kotlin", 10, Availability.Available, emptyList()),
				Consultant("4", "Docker", 12, Availability.Unavailable, emptyList()),
			)
		assertThat(values.rankAvailable().map { it.displayName })
			.containsExactly("Kotlin", "AWS", "Spring")
	}

	@Test
	fun `skillrapport teller og sorterer presist`() {
		val values =
			listOf(
				Consultant("1", "Kari", 10, Availability.Available, listOf("Kotlin", "AWS")),
				Consultant("2", "Ola", 6, Availability.Available, listOf("Kotlin", "Spring")),
			)
		assertThat(values.skillPopularity())
			.containsExactly(
				SkillPopularityDto("Kotlin", 2),
				SkillPopularityDto("AWS", 1),
				SkillPopularityDto("Spring", 1),
			)
	}

	@Test
	fun `tomme lister gir tomme resultater`() {
		assertThat(emptyList<ConsultantInput>().toDomainConsultants()).isEmpty()
		assertThat(emptyList<Consultant>().rankAvailable()).isEmpty()
		assertThat(emptyList<Consultant>().skillPopularity()).isEmpty()
	}
}
