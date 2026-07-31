/**
 * COROUTINES / STRUCTURED CONCURRENCY
 *
 * Dagens implementasjon er sekvensiell og naiv. Se README for TODO-er:
 * parallelliser med async, legg på timeout og avklar feil-/kanselleringssemantikk.
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
    private val cvClient: CvClient
) {

    // TODO: parallelliser med coroutineScope + async, og legg på timeout for CV-kallet
    suspend fun fetchSummary(id: ConsultantId): ConsultantSummary {
        val profile = profileClient.fetchProfile(id)
        val cv = cvClient.fetchCv(id)
        return ConsultantSummary(
            name = profile.name,
            role = profile.role,
            skills = cv.skills
        )
    }
}
