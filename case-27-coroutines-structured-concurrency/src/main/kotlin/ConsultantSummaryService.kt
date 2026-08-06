import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeoutOrNull

/**
 * COROUTINES / STRUCTURED CONCURRENCY
 *
 * Implementasjonen parallelliserer kallene med async og har eksplisitt timeout-
 * og kanselleringssemantikk.
 */

@JvmInline
value class ConsultantId(val value: String)

data class Profile(val consultantId: ConsultantId, val name: String, val role: String)

data class Cv(val consultantId: ConsultantId, val skills: List<String>)

data class ConsultantSummary(
    val name: String,
    val role: String,
    val skills: List<String>
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
    private val cvTimeoutMillis: Long = DEFAULT_CV_TIMEOUT_MILLIS
) {
    init {
        require(cvTimeoutMillis > 0) { "cvTimeoutMillis must be greater than zero" }
    }

    suspend fun fetchSummary(id: ConsultantId): ConsultantSummary = coroutineScope {
        val profileDeferred = async { profileClient.fetchProfile(id) }
        val cvDeferred = async {
            withTimeoutOrNull(cvTimeoutMillis) { cvClient.fetchCv(id) }
        }

        val profile = profileDeferred.await()
        val cv = cvDeferred.await()

        ConsultantSummary(
            name = profile.name,
            role = profile.role,
            skills = cv?.skills.orEmpty()
        )
    }

    private companion object {
        const val DEFAULT_CV_TIMEOUT_MILLIS = 1_500L
    }
}
