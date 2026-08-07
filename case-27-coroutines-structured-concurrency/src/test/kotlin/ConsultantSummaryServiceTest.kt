import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds

class ConsultantSummaryServiceTest {

    private val id = ConsultantId("C1")

    private val slowProfileClient = object : ProfileClient {
        override suspend fun fetchProfile(id: ConsultantId): Profile {
            delay(1000.milliseconds)
            return Profile(id, "Thomas", "Arkitekt")
        }
    }

    private val slowCvClient = object : CvClient {
        override suspend fun fetchCv(id: ConsultantId): Cv {
            delay(1000.milliseconds)
            return Cv(id, listOf("Kotlin", "Spring Boot", "DDD"))
        }
    }

    @Test
    fun `summary combines profile and cv`() = runTest {
        val service = ConsultantSummaryService(slowProfileClient, slowCvClient)

        val summary = service.fetchSummary(id)

        assertThat(summary.name).isEqualTo("Thomas")
        assertThat(summary.role).isEqualTo("Arkitekt")
        assertThat(summary.skills).contains("Kotlin")
    }

    /**
     * Denne testen FEILER med den sekvensielle implementasjonen (2000 ms virtuell tid).
     * Den skal bli grønn når du parallelliserer med coroutineScope + async.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetches profile and cv in parallel`() = runTest {
        val service = ConsultantSummaryService(slowProfileClient, slowCvClient)
        val start = currentTime

        service.fetchSummary(id)

        val elapsed = this.currentTime - start
        assertThat(elapsed)
            .describedAs("profil og CV skal hentes parallelt (forventet ~1000ms, ikke summen)")
            .isLessThan(2000)
    }

    // TODO: skriv en test som verifiserer at CV-kallet kanselleres når profilkallet feiler
    val failedProfileFetchText = "Failed to fetch profile for consultant with id=$id"

    @Test
    fun `cancels cv call when profile call fails`() = runTest {
        val cvStarted = CompletableDeferred<Unit>()
        var cvWasCanceled = false

        val failingProfileClient = object : ProfileClient {
            override suspend fun fetchProfile(id: ConsultantId): Profile {
                cvStarted.await()
                throw RuntimeException(failedProfileFetchText)
            }
        }

        val verySlowCvClient = object : CvClient {
            override suspend fun fetchCv(id: ConsultantId): Cv {
                cvStarted.complete(Unit)

                try {
                    awaitCancellation()
                } finally {
                    cvWasCanceled = true
                }
            }
        }


        val service = ConsultantSummaryService(
            failingProfileClient,
            verySlowCvClient
        )

        var thrownException: RuntimeException? = null

        try {
            service.fetchSummary(id)
        } catch (e: RuntimeException) {
            assertEquals(failedProfileFetchText, e.message)
            thrownException = e
        }

        assertThat(thrownException)
            .isNotNull
            .hasMessage(failedProfileFetchText)

        assertThat(cvStarted.isCompleted).isTrue
        assertThat(cvWasCanceled).isTrue
    }
}
