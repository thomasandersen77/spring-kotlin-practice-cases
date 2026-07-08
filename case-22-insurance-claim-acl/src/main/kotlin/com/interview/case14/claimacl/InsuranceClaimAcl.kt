package com.interview.case14.claimacl

import java.time.LocalDate

data class ExternalClaimDto(
    val claimTypeCode: String?,
    val amount: String?,
    val currency: String?,
    val customerId: String?,
    val incidentDate: String?
)

enum class ClaimType {
    CAR,
    HOUSE,
    TRAVEL
}

data class InsuranceClaim(
    val claimType: ClaimType,
    val customerId: String,
    val incidentDate: LocalDate
)

sealed class MappingResult {
    data class Success(val claim: InsuranceClaim) : MappingResult()
    data class Failure(val message: String) : MappingResult()
}

class ClaimTranslator {
    fun translate(dto: ExternalClaimDto): MappingResult {
        TODO("Implement anti-corruption translation with robust validation and explicit failure messages")
    }
}
