package com.interview.case09.triage

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HospitalTriagePolicyTest {
    private val policy = TriagePolicy()

    @Test
    fun `critical symptom should map to immediate`() {
        val request = PatientTriageRequest(
            ageInYears = 40,
            symptomSeverity = SymptomSeverity.CRITICAL,
            painScore = 5,
            waitingMinutes = 5,
            vitalSigns = VitalSigns(oxygenSaturation = 98, feverCelsius = 37.0)
        )

        val result = policy.prioritize(request)

        assertThat(result).isEqualTo(TriagePriority.IMMEDIATE)
    }
}
