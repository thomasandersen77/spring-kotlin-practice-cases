package com.training.case53.legal

enum class ClauseCategory {
 LIABILITY,
 TERMINATION,
 INTELLECTUAL_PROPERTY,
 CONFIDENTIALITY,
 DATA_PROCESSING,
 GOVERNING_LAW,
 OTHER
}

enum class RiskLevel { LOW, MEDIUM, HIGH }

data class ModelReviewProposal(
 val category: ClauseCategory,
 val riskLevel: RiskLevel,
 val summary: String,
 val evidence: String,
 val missingInformation: List<String>,
 val requiresHumanReview: Boolean
)

data class ContractReview(
 val category: ClauseCategory,
 val riskLevel: RiskLevel,
 val summary: String,
 val evidence: String,
 val missingInformation: List<String>,
 val requiresHumanReview: Boolean
) {
 companion object {
 // TODO nivå 1: Valider modellen mot originalteksten og håndhev domenets egne regler.
 // Hint: evidence må finnes ordrett i klausulen, og HIGH kan aldri godkjennes uten jurist.
 fun fromModel(clauseText: String, proposal: ModelReviewProposal): ContractReview =
 TODO("Valider modellforslaget før det blir et domeneobjekt")
 }
}

data class ReviewContractRequest(val clauseText: String)

class InvalidModelResponse(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
class OpenAiUnavailable(cause: Throwable? = null) : RuntimeException("OpenAI er midlertidig utilgjengelig", cause)
class ModelRefused : RuntimeException("Modellen avslo å vurdere klausulen")
