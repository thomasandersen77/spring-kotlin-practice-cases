import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class CvScoringServiceTest {

	private val primary = ModelId("gemini-3-pro-preview")
	private val fallback = ModelId("gemini-2.5-pro")

	private val cv = CvDocument("Thomas", "20 års erfaring med Kotlin, Spring Boot og DDD")
	private val request = ProjectRequest("Oslo kommune", "Senior Kotlin-utvikler med DDD-erfaring")

	/** Fake-adapter: full kontroll over feilscenarier, ingen nettverk eller API-nøkler. */
	private class FakeLlmClient(private val behaviour: (ModelId) -> ScoringResult) : LlmClient {
		val calls = mutableListOf<ModelId>()

		override fun scoreCv(
			model: ModelId,
			cv: CvDocument,
			request: ProjectRequest,
		): ScoringResult {
			calls.add(model)
			return behaviour(model)
		}
	}

	@Test
	fun `uses primary model when it responds`() {
		val client = FakeLlmClient { model ->
			ScoringResult(Score(85), "God match", model)
		}
		val service = CvScoringService(client, primary, fallback)

		val result = service.score(cv, request)

		assertThat(result.modelUsed).isEqualTo(primary)
		assertThat(client.calls).containsExactly(primary)
	}

	/** FEILER til fallback-logikken er implementert. */
	@Test
	fun `falls back to secondary model when primary is overloaded`() {
		val client = FakeLlmClient { model ->
			if (model == primary) throw LlmOverloadedException("503 fra leverandør")
			ScoringResult(Score(78), "God match (fallback)", model)
		}
		val service = CvScoringService(client, primary, fallback)

		val result = service.score(cv, request)

		assertThat(result.modelUsed).isEqualTo(fallback)
		assertThat(client.calls).containsExactly(primary, fallback)
	}

	@Test
	fun `does not fall back on invalid response - the bug is ours`() {
		val client = FakeLlmClient { _ ->
			throw LlmInvalidResponseException("Kunne ikke parse score fra svaret")
		}
		val service = CvScoringService(client, primary, fallback)

		assertThatThrownBy { service.score(cv, request) }
			.isInstanceOf(LlmInvalidResponseException::class.java)

		assertThat(client.calls)
			.describedAs("ugyldig respons skal ikke utløse fallback")
			.containsExactly(primary)
	}

	/** FEILER til Score valideres i init-blokken. */
	@Test
	fun `hallucinated score outside 0-100 is rejected at construction`() {
		assertThatThrownBy { Score(180) }.isInstanceOf(IllegalArgumentException::class.java)
		assertThatThrownBy { Score(-5) }.isInstanceOf(IllegalArgumentException::class.java)
	}
}
