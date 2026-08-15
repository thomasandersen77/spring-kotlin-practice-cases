package com.training.case53.legal

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Level2OpenAiWireMockTest(@Autowired private val adapter: OpenAiContractReviewAdapter) {
 companion object {
 private val wireMock = WireMockServer(options().dynamicPort()).apply { start() }

 @JvmStatic
 @DynamicPropertySource
 fun properties(registry: DynamicPropertyRegistry) {
 registry.add("openai.api-key") { "test-key" }
 registry.add("openai.base-url") { "${wireMock.baseUrl()}/v1" }
 registry.add("openai.model") { "gpt-5.6-luna" }
 registry.add("openai.max-output-tokens") { 600 }
 }
 }

 @BeforeEach
 fun reset() = wireMock.resetAll()

 @AfterAll
 fun stop() = wireMock.stop()

 @Test
 fun `adapteren bruker Responses API modellvalg structured output og bearer token`() {
 wireMock.stubFor(
 post(urlEqualTo("/v1/responses"))
 .willReturn(okJson(successfulResponse()))
 )

 val result = adapter.review(
 LegalReviewPrompt(
 instructions = "Utfør juridisk triage. Ikke gi juridisk rådgivning.",
 clauseText = "Avtalen kan sies opp med 30 dagers varsel."
 )
 )

 assertThat(result.category).isEqualTo(ClauseCategory.TERMINATION)
 assertThat(result.evidence).isEqualTo("sies opp med 30 dagers varsel")
 wireMock.verify(
 postRequestedFor(urlEqualTo("/v1/responses"))
 .withHeader("Authorization", equalTo("Bearer test-key"))
 .withRequestBody(matchingJsonPath("$.model", equalTo("gpt-5.6-luna")))
 .withRequestBody(matchingJsonPath("$.instructions", containing("juridisk triage")))
 .withRequestBody(matchingJsonPath("$.input", containing("30 dagers varsel")))
 .withRequestBody(matchingJsonPath("$.text.format.type", equalTo("json_schema")))
 )
 }

 @Test
 fun `rate limit oversettes til midlertidig utilgjengelig`() {
 wireMock.stubFor(
 post(urlEqualTo("/v1/responses")).willReturn(
 aResponse()
 .withStatus(429)
 .withHeader("Content-Type", "application/json")
 .withBody("""{"error":{"message":"rate limited","type":"rate_limit_error","code":"rate_limit_exceeded"}}""")
 )
 )

 org.assertj.core.api.Assertions.assertThatThrownBy {
 adapter.review(LegalReviewPrompt("Instruksjoner", "Klausul"))
 }.isInstanceOf(OpenAiUnavailable::class.java)
 }

 // TODO nivå 2+: Legg selv til kontrakttester for 500, ugyldig output, refusal og tom output.

 private fun successfulResponse(): String =
 """
 {
 "id": "resp_case53",
 "object": "response",
 "created_at": 1720000000,
 "status": "completed",
 "error": null,
 "incomplete_details": null,
 "instructions": "Utfør juridisk triage.",
 "max_output_tokens": 600,
 "model": "gpt-5.6-luna",
 "output": [
 {
 "id": "msg_case53",
 "type": "message",
 "status": "completed",
 "role": "assistant",
 "content": [
 {
 "type": "output_text",
 "annotations": [],
 "text": "{\"category\":\"TERMINATION\",\"riskLevel\":\"MEDIUM\",\"summary\":\"Oppsigelsesfrist er angitt.\",\"evidence\":\"sies opp med 30 dagers varsel\",\"missingInformation\":[],\"requiresHumanReview\":true}"
 }
 ]
 }
 ],
 "parallel_tool_calls": true,
 "previous_response_id": null,
 "store": false,
 "temperature": 1.0,
 "tool_choice": "auto",
 "tools": [],
 "top_p": 1.0,
 "truncation": "disabled",
 "usage": {
 "input_tokens": 50,
 "input_tokens_details": {"cached_tokens": 0},
 "output_tokens": 30,
 "output_tokens_details": {"reasoning_tokens": 0},
 "total_tokens": 80
 }
 }
 """.trimIndent()
}
