package com.training.case14.claimacl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class InsuranceClaimAclTest {
 private val translator = ClaimTranslator()

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
 }
}
