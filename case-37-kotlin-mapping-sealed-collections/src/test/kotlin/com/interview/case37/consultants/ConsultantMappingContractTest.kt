package com.interview.case37.consultants

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class ConsultantMappingContractTest {

    @Test
    fun `partial status requires percentage strictly between zero and one hundred`() {
        assertThat(availabilityFrom("PARTIAL", null)).isEqualTo(Availability.Unknown("PARTIAL"))
        assertThat(availabilityFrom("PARTIAL", 0)).isEqualTo(Availability.Unknown("PARTIAL"))
        assertThat(availabilityFrom("PARTIAL", 100)).isEqualTo(Availability.Unknown("PARTIAL"))

        assertThatIllegalArgumentException()
            .isThrownBy { Availability.PartiallyAvailable(0) }
            .withMessage("Partial availability must be between 1 and 99 percent")
    }

    @Test
    fun `dto labels every availability variant`() {
        fun dtoFor(availability: Availability): ConsultantDto =
            Consultant("1", "Kari", 10, availability, emptyList()).toDto()

        assertThat(dtoFor(Availability.Available).availabilityLabel).isEqualTo("Tilgjengelig")
        assertThat(dtoFor(Availability.PartiallyAvailable(60)).availabilityLabel)
            .isEqualTo("Delvis tilgjengelig (60 %)")
        assertThat(dtoFor(Availability.Unavailable).availabilityLabel).isEqualTo("Ikke tilgjengelig")
        assertThat(dtoFor(Availability.Unknown("NEW")).availabilityLabel).isEqualTo("Ukjent (NEW)")
        assertThat(dtoFor(Availability.Unknown(null)).availabilityLabel).isEqualTo("Ukjent")
    }

    @Test
    fun `skill popularity counts each consultant once per skill`() {
        val consultant = Consultant(
            id = "1",
            name = "Kari",
            yearsOfExperience = 10,
            availability = Availability.Available,
            certifiedSkills = listOf("Kotlin", "Kotlin")
        )

        assertThat(listOf(consultant).skillPopularity())
            .containsExactly(SkillPopularityDto("Kotlin", 1))
    }
}
