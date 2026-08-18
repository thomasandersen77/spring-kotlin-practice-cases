import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeoutOrNull

/**
 * COROUTINES / STRUCTURED CONCURRENCY
 *
 * Dagens implementasjon er sekvensiell og naiv. Se README for TODO-er: parallelliser med async,
 * legg på timeout og avklar feil-/kanselleringssemantikk.
 */
@JvmInline value class ConsultantId(val value: String)

data class Profile(val consultantId: ConsultantId, val name: String, val role: String)

data class Cv(val consultantId: ConsultantId, val skills: List<String>)

data class ConsultantSummary(
	val name: String,
	val role: String,
	val skills: List<String>,
)

/** Simulerer et tregt nettverkskall (implementeres med delay i test/fakes). */
interface ProfileClient {
	suspend fun fetchProfile(id: ConsultantId): Profile
}

interface CvClient {
	suspend fun fetchCv(id: ConsultantId): Cv
}

class ConsultantSummaryService(
	private val profileClient: ProfileClient,
	private val cvClient: CvClient,
) {

	// TODO: parallelliser med coroutineScope + async, og legg på timeout for CV-kallet
	suspend fun fetchSummary(id: ConsultantId): ConsultantSummary = coroutineScope {
		val profileDeferred = async {
			profileClient.fetchProfile(id)
		}

		val cvDeferred = async {
			withTimeoutOrNull(timeMillis = 2000L) {
				cvClient.fetchCv(id)
			}
		}

		val profile = profileDeferred.await()
		val cv = cvDeferred.await()

		ConsultantSummary(
			name = profile.name,
			role = profile.role,
			skills = cv?.skills ?: emptyList(),
		)
	}
}
