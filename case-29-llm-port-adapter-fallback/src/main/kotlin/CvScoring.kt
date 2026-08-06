/**
 * LLM BAK PORT/ADAPTER MED MODELL-FALLBACK
 *
 * Domenet kjenner kun porten LlmClient — aldri HTTP, JSON eller leverandørnavn.
 * Dagens CvScoringService kaller primærmodellen direkte uten feilhåndtering.
 * Se README for TODO-er.
 */

@JvmInline
value class ModelId(val value: String) {
    init { require(value.isNotBlank()) { "model id cannot be blank" } }
}

@JvmInline
value class Score(val value: Int) {
    init {
        require(value in 0..100) { "score must be between 0 and 100" }
    }
}

data class CvDocument(val consultantName: String, val text: String)

data class ProjectRequest(val customer: String, val requirements: String)

data class ScoringResult(
    val score: Score,
    val justification: String,
    val modelUsed: ModelId
)

/** Kastes av adapteren når leverandøren svarer 503 / er overbelastet. */
class LlmOverloadedException(message: String) : RuntimeException(message)

/** Kastes av adapteren når svaret ikke lar seg parse til et gyldig resultat. */
class LlmInvalidResponseException(message: String) : RuntimeException(message)

/**
 * Porten. Adapteren (ikke del av dette caset) ville implementert denne mot
 * Gemini/OpenAI over HTTP. I testene brukes fakes.
 */
interface LlmClient {
    fun scoreCv(model: ModelId, cv: CvDocument, request: ProjectRequest): ScoringResult
}

class CvScoringService(
    private val llmClient: LlmClient,
    private val primaryModel: ModelId,
    private val fallbackModel: ModelId
) {

    /**
     * TODO: implementer kontrakten fra README/testene:
     *  - prøv primærmodellen først
     *  - ved LlmOverloadedException: bruk fallback-modellen
     *  - ved LlmInvalidResponseException: IKKE fall tilbake, la feilen propagere
     *  - resultatet skal vise hvilken modell som faktisk ble brukt
     */
    fun score(cv: CvDocument, request: ProjectRequest): ScoringResult {
        require(cv.consultantName.isNotBlank() && cv.text.isNotBlank()) { "CV must contain name and text" }
        require(request.customer.isNotBlank() && request.requirements.isNotBlank()) {
            "project request must contain customer and requirements"
        }
        return try {
            llmClient.scoreCv(primaryModel, cv, request)
        } catch (_: LlmOverloadedException) {
            llmClient.scoreCv(fallbackModel, cv, request)
        }
    }
}
