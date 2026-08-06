package com.interview.case14.claimacl

import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate
import java.util.Currency

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
    val incidentDate: LocalDate,
    val amount: BigDecimal,
    val currency: Currency
)

sealed class MappingResult {
    data class Success(val claim: InsuranceClaim) : MappingResult()
    data class Failure(val message: String) : MappingResult()
}

class ClaimTranslator(private val clock: Clock = Clock.systemDefaultZone()) {
    fun translate(dto: ExternalClaimDto): MappingResult {
        val type = when (dto.claimTypeCode?.trim()?.uppercase()) {
            "CAR" -> ClaimType.CAR
            "HOUSE", "HOME" -> ClaimType.HOUSE
            "TRAVEL" -> ClaimType.TRAVEL
            null, "" -> return MappingResult.Failure("claim type is required")
            else -> return MappingResult.Failure("unknown claim type: ${dto.claimTypeCode}")
        }
        val customerId = dto.customerId?.trim()?.takeIf(String::isNotEmpty)
            ?: return MappingResult.Failure("customer id is required")
        val incidentDate = try { LocalDate.parse(dto.incidentDate) }
        catch (_: Exception) { return MappingResult.Failure("incident date must use ISO format yyyy-MM-dd") }
        if (incidentDate.isAfter(LocalDate.now(clock))) return MappingResult.Failure("incident date cannot be in the future")
        val amount = try { dto.amount?.let(::BigDecimal) }
        catch (_: NumberFormatException) { null }
            ?: return MappingResult.Failure("amount must be a decimal number")
        if (amount.signum() <= 0) return MappingResult.Failure("amount must be positive")
        val currency = try { dto.currency?.trim()?.uppercase()?.let(Currency::getInstance) }
        catch (_: IllegalArgumentException) { null }
            ?: return MappingResult.Failure("currency must be a valid ISO code")

        return MappingResult.Success(InsuranceClaim(type, customerId, incidentDate, amount, currency))
    }
}
