package com.interview.case14.claimacl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class InsuranceClaimAclTest {
    private val translator = ClaimTranslator(Clock.fixed(Instant.parse("2026-02-01T00:00:00Z"), ZoneOffset.UTC))

    @Test
    fun `valid dto should map to success`() {
        val dto = ExternalClaimDto(
            claimTypeCode = "CAR",
            amount = "1000.00",
            currency = "NOK",
            customerId = "CUST-1",
            incidentDate = "2026-01-01"
        )

        val result = translator.translate(dto)

        assertThat(result).isInstanceOf(MappingResult.Success::class.java)
        val claim = (result as MappingResult.Success).claim
        assertThat(claim.claimType).isEqualTo(ClaimType.CAR)
        assertThat(claim.amount).isEqualByComparingTo("1000.00")
        assertThat(claim.currency.currencyCode).isEqualTo("NOK")
    }

    @Test
    fun `unknown type invalid amount and currency should be controlled failures`() {
        assertFailure(dto(type = "BOAT"), "unknown claim type")
        assertFailure(dto(amount = "not-money"), "amount")
        assertFailure(dto(currency = "NOPE"), "currency")
    }

    @Test
    fun `missing customer invalid date and future incident should be controlled failures`() {
        assertFailure(dto(customer = null), "customer")
        assertFailure(dto(date = "01-01-2026"), "ISO")
        assertFailure(dto(date = "2026-02-02"), "future")
    }

    private fun dto(
        type: String? = "CAR", amount: String? = "1000.00", currency: String? = "NOK",
        customer: String? = "CUST-1", date: String? = "2026-01-01"
    ) = ExternalClaimDto(type, amount, currency, customer, date)

    private fun assertFailure(dto: ExternalClaimDto, message: String) {
        val result = translator.translate(dto)
        assertThat(result).isInstanceOf(MappingResult.Failure::class.java)
        assertThat((result as MappingResult.Failure).message).containsIgnoringCase(message)
    }
}
