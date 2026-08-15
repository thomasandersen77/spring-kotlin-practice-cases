package com.training.case40.security
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant

@DataJpaTest
class SecurityPersistenceTest(@Autowired val cases: CaseRepository, @Autowired val audits: AuditRepository) {
 @Test fun `sak og audit kan lagres`() { val saved=cases.save(CaseEntity(ownerSubject="u1")); audits.save(AuditEntity(caseId=saved.id!!,actorSubject="m1",correlationId="c1",createdAt=Instant.now())); assertThat(cases.count()).isEqualTo(1); assertThat(audits.count()).isEqualTo(1) }
}
