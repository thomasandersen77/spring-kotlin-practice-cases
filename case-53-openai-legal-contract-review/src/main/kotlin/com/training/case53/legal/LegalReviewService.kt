package com.training.case53.legal

import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

data class LegalReviewPrompt(val instructions: String, val clauseText: String)

@Component
class LegalReviewPromptFactory {
	// TODO nivå 1: Bygg stabile instruksjoner som uttrykker domenepolicyen.
	// Hold klausulteksten ute av instructions; den er ubetrodd input og sendes separat.
	fun create(clauseText: String): LegalReviewPrompt = TODO("Bygg juridisk triage-prompt")
}

fun interface ContractReviewPort {
	fun review(prompt: LegalReviewPrompt): ModelReviewProposal
}

@Service
class LegalReviewService(
	private val promptFactory: LegalReviewPromptFactory,
	private val reviewPort: ContractReviewPort,
) {
	// TODO nivå 1: Valider request, kall porten og konstruer et validert ContractReview.
	fun review(clauseText: String): ContractReview = TODO("Orkestrer kontraktstriage")
}
