package com.interview.case09.triage

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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

    @Test
    fun `dangerously low oxygen should be immediate regardless of other findings`() {
        assertThat(policy.prioritize(request(oxygen = 89))).isEqualTo(TriagePriority.IMMEDIATE)
    }

    @Test
    fun `high pain fever long wait and vulnerable age with finding should be urgent`() {
        assertThat(policy.prioritize(request(pain = 7))).isEqualTo(TriagePriority.URGENT)
        assertThat(policy.prioritize(request(fever = 39.5))).isEqualTo(TriagePriority.URGENT)
        assertThat(policy.prioritize(request(waiting = 120))).isEqualTo(TriagePriority.URGENT)
        assertThat(policy.prioritize(request(age = 80, pain = 5))).isEqualTo(TriagePriority.URGENT)
    }

    @Test
    fun `age alone and normal findings should be standard`() {
        assertThat(policy.prioritize(request(age = 80))).isEqualTo(TriagePriority.STANDARD)
        assertThat(policy.prioritize(request())).isEqualTo(TriagePriority.STANDARD)
    }

    @Test
    fun `invalid domain values should be rejected`() {
        assertThatThrownBy { policy.prioritize(request(age = -1)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { policy.prioritize(request(pain = 11)) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { policy.prioritize(request(oxygen = 101)) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    private fun request(
        age: Int = 40,
        pain: Int = 2,
        waiting: Int = 10,
        oxygen: Int? = 98,
        fever: Double? = 37.0
    ) = PatientTriageRequest(age, SymptomSeverity.NORMAL, pain, waiting, VitalSigns(oxygen, fever))
}
