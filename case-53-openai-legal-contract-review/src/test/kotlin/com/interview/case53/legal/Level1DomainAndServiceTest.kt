package com.interview.case53.legal

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class Level1DomainAndServiceTest {
    private val clause = "Leverandørens samlede ansvar er begrenset til NOK 100 000."

    @Test
    fun `prompten har domeneregler og holder klausulen separat fra instruksjonene`() {
        val prompt = LegalReviewPromptFactory().create(clause)

        assertThat(prompt.clauseText).isEqualTo(clause)
        assertThat(prompt.instructions).doesNotContain(clause)
        assertThat(prompt.instructions).containsIgnoringCase("juridisk rådgivning")
        assertThat(prompt.instructions).containsIgnoringCase("ikke dikt")
        assertThat(prompt.instructions).containsIgnoringCase("ordrett")
        assertThat(prompt.instructions).containsIgnoringCase("menneskelig vurdering")
        assertThat(prompt.instructions).containsIgnoringCase("ubetrodd")
    }

    @Test
    fun `evidence som ikke finnes i klausulen avvises`() {
        val proposal = proposal(evidence = "Et utdrag som modellen fant på")

        assertThatThrownBy { ContractReview.fromModel(clause, proposal) }
            .isInstanceOf(InvalidModelResponse::class.java)
    }

    @Test
    fun `hoy risiko krever alltid menneskelig vurdering`() {
        val proposal = proposal(
            riskLevel = RiskLevel.HIGH,
            requiresHumanReview = false
        )

        val review = ContractReview.fromModel(clause, proposal)

        assertThat(review.requiresHumanReview).isTrue()
    }

    @Test
    fun `servicen sender prompt til port og validerer modellforslaget`() {
        var receivedPrompt: LegalReviewPrompt? = null
        val port = ContractReviewPort { prompt ->
            receivedPrompt = prompt
            proposal()
        }
        val service = LegalReviewService(LegalReviewPromptFactory(), port)

        val review = service.review(clause)

        assertThat(receivedPrompt?.clauseText).isEqualTo(clause)
        assertThat(review.category).isEqualTo(ClauseCategory.LIABILITY)
    }

    private fun proposal(
        evidence: String = "samlede ansvar er begrenset til NOK 100 000",
        riskLevel: RiskLevel = RiskLevel.MEDIUM,
        requiresHumanReview: Boolean = true
    ) = ModelReviewProposal(
        category = ClauseCategory.LIABILITY,
        riskLevel = riskLevel,
        summary = "Ansvarsbegrensning er angitt.",
        evidence = evidence,
        missingInformation = listOf("Avtalens totalverdi"),
        requiresHumanReview = requiresHumanReview
    )
}
