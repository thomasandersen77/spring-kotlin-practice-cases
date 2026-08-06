import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ConsultantSummaryServiceContractTest {

    private val id = ConsultantId("C1")

    @Test
    fun `slow cv degrades to empty skills at configured timeout`() = runTest {
        val profileClient = object : ProfileClient {
            override suspend fun fetchProfile(id: ConsultantId): Profile =
                Profile(id, "Thomas", "Arkitekt")
        }
        val cvClient = object : CvClient {
            override suspend fun fetchCv(id: ConsultantId): Cv {
                delay(2_000)
                return Cv(id, listOf("Kotlin"))
            }
        }
        val service = ConsultantSummaryService(profileClient, cvClient, cvTimeoutMillis = 500)
        val start = currentTime

        val summary = service.fetchSummary(id)

        assertThat(summary.skills).isEmpty()
        assertThat(currentTime - start).isEqualTo(500)
    }

    @Test
    fun `profile failure cancels cv call before it completes`() = runTest {
        var cvCompleted = false
        var cvCancelled = false
        val profileClient = object : ProfileClient {
            override suspend fun fetchProfile(id: ConsultantId): Profile {
                delay(100)
                error("profile unavailable")
            }
        }
        val cvClient = object : CvClient {
            override suspend fun fetchCv(id: ConsultantId): Cv {
                try {
                    delay(1_000)
                    cvCompleted = true
                    return Cv(id, listOf("Kotlin"))
                } finally {
                    cvCancelled = !currentCoroutineContext().isActive
                }
            }
        }
        val service = ConsultantSummaryService(profileClient, cvClient)

        val failure = runCatching { service.fetchSummary(id) }.exceptionOrNull()

        assertThat(failure)
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("profile unavailable")
        assertThat(cvCompleted).isFalse()
        assertThat(cvCancelled).isTrue()
    }
}
