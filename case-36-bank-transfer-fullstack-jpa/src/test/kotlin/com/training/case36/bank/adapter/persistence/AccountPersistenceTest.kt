package com.training.case36.bank.adapter.persistence

import com.training.case36.bank.domain.AccountStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.Instant
import java.util.UUID

/**
 * `@DataJpaTest` wraps each test method in its own transaction that is rolled back automatically
 * afterwards, so test data never leaks between methods here - unlike the service-level transaction
 * test, which deliberately avoids that behaviour (see `BankingServiceTransactionIntegrationTest`).
 *
 * `@AutoConfigureTestDatabase(replace = NONE)` keeps our own H2/PostgreSQL-mode datasource from
 * `application-test.yml` instead of Spring Boot substituting its own anonymous embedded database.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AccountPersistenceTest {

 @Autowired
 lateinit var accountJpaRepository: AccountJpaRepository

 @Autowired
 lateinit var transferJpaRepository: TransferJpaRepository

 @Test
 fun `hibernate oppretter de forventede tabellene`() {
 assertThat(accountJpaRepository.count()).isZero()
 assertThat(transferJpaRepository.count()).isZero()
 }

 @Test
 fun `konto kan lagres og leses tilbake`() {
 val saved = accountJpaRepository.save(
 AccountJpaEntity(
 id = UUID.randomUUID(),
 ownerName = "Kari Nordmann",
 status = AccountStatus.ACTIVE,
 balanceOre = 0
 )
 )

 val fetched = accountJpaRepository.findById(saved.id).orElseThrow()
 assertThat(fetched.ownerName).isEqualTo("Kari Nordmann")
 assertThat(fetched.status).isEqualTo(AccountStatus.ACTIVE)
 }

 @Test
 fun `saldo i ore bevares eksakt`() {
 val saved = accountJpaRepository.saveAndFlush(
 AccountJpaEntity(
 id = UUID.randomUUID(),
 ownerName = "Ola Hansen",
 status = AccountStatus.ACTIVE,
 balanceOre = 123_456
 )
 )

 val fetched = accountJpaRepository.findById(saved.id).orElseThrow()
 assertThat(fetched.balanceOre).isEqualTo(123_456L)
 }

 @Test
 fun `version oppdateres ved endring`() {
 val saved = accountJpaRepository.saveAndFlush(
 AccountJpaEntity(
 id = UUID.randomUUID(),
 ownerName = "Liv Berg",
 status = AccountStatus.ACTIVE,
 balanceOre = 0
 )
 )
 val versionAfterInsert = saved.version

 saved.balanceOre = 500
 val updated = accountJpaRepository.saveAndFlush(saved)

 assertThat(updated.version).isGreaterThan(versionAfterInsert)
 }

 @Test
 fun `overforingsrad kan lagres med fra- og til-konto og belop`() {
 val fromId = UUID.randomUUID()
 val toId = UUID.randomUUID()

 val saved = transferJpaRepository.save(
 TransferJpaEntity(
 id = UUID.randomUUID(),
 fromAccountId = fromId,
 toAccountId = toId,
 amountOre = 25_000,
 executedAt = Instant.parse("2026-08-03T12:00:00Z")
 )
 )

 val fetched = transferJpaRepository.findById(saved.id).orElseThrow()
 assertThat(fetched.fromAccountId).isEqualTo(fromId)
 assertThat(fetched.toAccountId).isEqualTo(toId)
 assertThat(fetched.amountOre).isEqualTo(25_000L)
 }

 @Test
 fun `mapping fra JPA til domene bevarer relevante data`() {
 val entity = accountJpaRepository.saveAndFlush(
 AccountJpaEntity(
 id = UUID.randomUUID(),
 ownerName = "Per Ås",
 status = AccountStatus.BLOCKED,
 balanceOre = 4_200
 )
 )

 val domain = entity.toDomain()

 assertThat(domain.id.value).isEqualTo(entity.id)
 assertThat(domain.ownerName).isEqualTo("Per Ås")
 assertThat(domain.status).isEqualTo(AccountStatus.BLOCKED)
 assertThat(domain.balance.amountOre).isEqualTo(4_200L)
 }

 @Test
 fun `mapping fra transfer-entitet til domene bevarer relevante data`() {
 val entity = transferJpaRepository.saveAndFlush(
 TransferJpaEntity(
 id = UUID.randomUUID(),
 fromAccountId = UUID.randomUUID(),
 toAccountId = UUID.randomUUID(),
 amountOre = 9_900,
 executedAt = Instant.parse("2026-08-03T15:30:00Z")
 )
 )

 val domain = entity.toDomain()

 assertThat(domain.id.value).isEqualTo(entity.id)
 assertThat(domain.fromAccountId.value).isEqualTo(entity.fromAccountId)
 assertThat(domain.toAccountId.value).isEqualTo(entity.toAccountId)
 assertThat(domain.amount.amountOre).isEqualTo(9_900L)
 assertThat(domain.executedAt).isEqualTo(entity.executedAt)
 }
}
