package com.training.case36.bank.config

import com.training.case36.bank.adapter.persistence.AccountJpaEntity
import com.training.case36.bank.adapter.persistence.AccountJpaRepository
import com.training.case36.bank.domain.AccountStatus
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

/**
 * Documents the `spring.jpa.hibernate.ddl-auto=create-drop` lifecycle: Hibernate creates the schema
 * when the `EntityManagerFactory`/Spring context starts, and drops it when that context closes.
 *
 * Spring normally CACHES the test context between test methods (and even between test classes with
 * an identical configuration) precisely so that create-drop does NOT run before every single test
 * method in the whole suite - that would make the suite slow for no benefit. This one small,
 * dedicated class is the deliberate exception: `@DirtiesContext(classMode =
 * AFTER_EACH_TEST_METHOD)` forces Spring to throw away and rebuild the context (and therefore the
 * H2 schema) after every test method in THIS class only, so we can demonstrate the lifecycle
 * directly. No other test class in this case uses `@DirtiesContext` - see
 * `BankingServiceTransactionIntegrationTest` for how spy-based test isolation is done instead,
 * without paying the cost of restarting the whole Spring context.
 */
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class HibernateSchemaLifecycleIntegrationTest {

	@Autowired lateinit var accountJpaRepository: AccountJpaRepository

	@Test
	fun `hibernate oppretter et tomt skjema nar konteksten starter`() {
		assertThat(accountJpaRepository.count()).isZero()
	}

	@Test
	fun `hver testmetode her far en fersk kontekst og dermed et ferskt skjema`() {
		// Hvis denne testen delte skjema med testen over uten @DirtiesContext, ville en rad lagret
		// der
		// fortsatt vaere synlig her. @DirtiesContext(AFTER_EACH_TEST_METHOD) garanterer at det ikke
		// er
		// tilfelle, uansett kjorerekkefolge.
		accountJpaRepository.save(
			AccountJpaEntity(
				id = UUID.randomUUID(),
				ownerName = "Kun i denne testen",
				status = AccountStatus.ACTIVE,
				balanceOre = 0,
			)
		)
		assertThat(accountJpaRepository.count()).isEqualTo(1)
	}
}
