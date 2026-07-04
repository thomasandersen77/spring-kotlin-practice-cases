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
        TODO("Implement triage rule ordering and edge cases")
    }
}
