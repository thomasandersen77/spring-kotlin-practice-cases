package com.training.case39.capacity

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CapacityWireMockTest(@Autowired val adapter: RestClientCapacityAdapter) {
 companion object {
 private val wireMock = WireMockServer(options().dynamicPort()).apply { start() }
 @JvmStatic
 @DynamicPropertySource
 fun properties(registry: DynamicPropertyRegistry) {
 registry.add("provider.capacity.base-url") { wireMock.baseUrl() }
 }
 }

 @BeforeEach
 fun reset() = wireMock.resetAll()
 @AfterAll
 fun stop() = wireMock.stop()

 @Test
 fun `200 mappes og riktig path og header sendes`() {
 wireMock.stubFor(get(urlEqualTo("/external/consultants/C-1/capacity")).willReturn(okJson("""{"consultant_ref":"C-1","state_code":"FREE","periods":[],"skills":["Kotlin"]}""")))
 assertThat(adapter.getCapacity("C-1").availability).isEqualTo(Availability.Available)
 wireMock.verify(
 getRequestedFor(urlEqualTo("/external/consultants/C-1/capacity")).withHeader(
 "X-Api-Key",
 equalTo("training-key")
 )
 )
 }

 @Test
 fun `ekstern 404 blir ikke funnet`() {
 wireMock.stubFor(get(anyUrl()).willReturn(notFound())); assertThatThrownBy { adapter.getCapacity("404") }.isInstanceOf(
 ConsultantNotFound::class.java
 )
 }

 @Test
 fun `ekstern 500 blir utilgjengelig leverandor`() {
 wireMock.stubFor(get(anyUrl()).willReturn(serverError())); assertThatThrownBy { adapter.getCapacity("500") }.isInstanceOf(
 ProviderUnavailable::class.java
 )
 }

 @Test
 fun `ugyldig json blir ugyldig leverandorrespons`() {
 wireMock.stubFor(get(anyUrl()).willReturn(okJson("{"))); assertThatThrownBy { adapter.getCapacity("bad") }.isInstanceOf(
 InvalidProviderResponse::class.java
 )
 }

 @Test
 fun `ukjent statuskode avvises`() {
 wireMock.stubFor(get(anyUrl()).willReturn(okJson("""{"consultant_ref":"C-1","state_code":"ALIEN","periods":[],"skills":[]}"""))); assertThatThrownBy {
 adapter.getCapacity(
 "C-1"
 )
 }.isInstanceOf(InvalidProviderResponse::class.java)
 }

 @Test
 fun `treg leverandor oversettes til utilgjengelig`() {
 wireMock.stubFor(get(anyUrl()).willReturn(okJson("{}").withFixedDelay(1500))); assertThatThrownBy {
 adapter.getCapacity(
 "slow"
 )
 }.isInstanceOf(ProviderUnavailable::class.java)
 }
}
