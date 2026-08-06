package com.interview.case09.triage

enum class SymptomSeverity { NORMAL, CRITICAL }
enum class TriagePriority { IMMEDIATE, URGENT, STANDARD }

data class VitalSigns(val oxygenSaturation: Int?, val feverCelsius: Double?)
data class PatientTriageRequest(
    val ageInYears: Int,
    val symptomSeverity: SymptomSeverity,
    val painScore: Int,
    val waitingMinutes: Int,
    val vitalSigns: VitalSigns
)

class TriagePolicy {
    fun prioritize(request: PatientTriageRequest): TriagePriority {
        validate(request)

        if (request.symptomSeverity == SymptomSeverity.CRITICAL ||
            request.vitalSigns.oxygenSaturation?.let { it < 90 } == true
        ) return TriagePriority.IMMEDIATE

        if (request.vitalSigns.oxygenSaturation?.let { it < 95 } == true ||
            request.vitalSigns.feverCelsius?.let { it >= 39.5 } == true ||
            request.painScore >= 7 ||
            request.waitingMinutes >= 120 ||
            request.isVulnerableWithConcerningFinding()
        ) return TriagePriority.URGENT

        return TriagePriority.STANDARD
    }

    private fun validate(request: PatientTriageRequest) {
        require(request.ageInYears in 0..130) { "age must be between 0 and 130" }
        require(request.painScore in 0..10) { "pain score must be between 0 and 10" }
        require(request.waitingMinutes >= 0) { "waiting time cannot be negative" }
        request.vitalSigns.oxygenSaturation?.let {
            require(it in 0..100) { "oxygen saturation must be between 0 and 100" }
        }
        request.vitalSigns.feverCelsius?.let {
            require(it in 30.0..45.0) { "temperature must be between 30 and 45" }
        }
    }

    private fun PatientTriageRequest.isVulnerableWithConcerningFinding(): Boolean {
        val vulnerableAge = ageInYears < 1 || ageInYears >= 75
        val concerningFinding = painScore >= 5 || vitalSigns.feverCelsius?.let { it >= 38.0 } == true
        return vulnerableAge && concerningFinding
    }
}
