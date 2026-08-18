package com.training.case53.legal

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Koster penger og treffer en virkelig ekstern tjeneste. Bruk bare syntetiske data. Testen er
 * deaktivert med mindre RUN_OPENAI_LIVE_TEST=true finnes i prosessmiljøet.
 */
@Tag("live")
@SpringBootTest
@EnabledIfEnvironmentVariable(named = "RUN_OPENAI_LIVE_TEST", matches = "true")
class Level3OpenAiLiveSmokeTest(
	@Autowired private val adapter: OpenAiContractReviewAdapter,
	@Autowired private val promptFactory: LegalReviewPromptFactory,
) {
	@Test
	fun `kan hente strukturert kontraktstriage fra valgt OpenAI-modell`() {
		val clause = "Leverandøren kan si opp avtalen med 14 dagers skriftlig varsel."

		val result = adapter.review(promptFactory.create(clause))

		assertThat(result.summary).isNotBlank()
		assertThat(result.evidence).isNotBlank()
		assertThat(clause).contains(result.evidence)
	}
}
