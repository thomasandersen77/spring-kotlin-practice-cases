import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConsultantSummaryServiceTest {

	private val id = ConsultantId("C1")

	private val slowProfileClient =
		object : ProfileClient {
			override suspend fun fetchProfile(id: ConsultantId): Profile {
				delay(1000)
				return Profile(id, "Thomas", "Arkitekt")
			}
		}

	private val slowCvClient =
		object : CvClient {
			override suspend fun fetchCv(id: ConsultantId): Cv {
				delay(1000)
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
	 * Denne testen FEILER med den sekvensielle implementasjonen (2000 ms virtuell tid). Den skal
	 * bli grønn når du parallelliserer med coroutineScope + async.
	 */
	@OptIn(ExperimentalCoroutinesApi::class)
	@Test
	fun `fetches profile and cv in parallel`() = runTest {
		val service = ConsultantSummaryService(slowProfileClient, slowCvClient)
		val start = currentTime

		service.fetchSummary(id)

		val elapsed = currentTime - start
		assertThat(elapsed)
			.describedAs("profil og CV skal hentes parallelt (forventet ~1000ms, ikke summen)")
			.isLessThan(2000)
	}

	// TODO: skriv en test som verifiserer at CV-kallet kanselleres når profilkallet feiler
}
