package com.interview.case39.capacity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CapacityMappingAndServiceTest {
    @Test
    fun `ekstern modell oversettes til domenesprak og sorterte skills`() {
        val external = ExternalCapacityResponse(
            "C-1",
            "FREE",
            listOf(ExternalPeriod("2026-09-01", "2026-09-30", 100)),
            listOf("Spring", "Kotlin")
        )
        assertThat(external.toDomain()).isEqualTo(
            ConsultantCapacity(
                "C-1",
                Availability.Available,
                listOf(CapacityPeriod("2026-09-01", "2026-09-30", 100)),
                listOf("Kotlin", "Spring")
            )
        )
    }

    @Test
    fun `service avhenger bare av port og intern modell`() {
        val service = CapacityService(CapacityPort {
            ConsultantCapacity(
                it,
                Availability.PartiallyAvailable,
                emptyList(),
                listOf("Kotlin")
            )
        })
        assertThat(service.get("C-2")).isEqualTo(CapacityResponse("C-2", "DELVIS", null, listOf("Kotlin")))
    }
}
