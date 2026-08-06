package com.interview.case40.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Optional

class CaseServiceTest {
    @Test fun `service mottar current user eksplisitt og returnerer tillatt sak`() {
        val cases = mock(CaseRepository::class.java)
        val audits = mock(AuditRepository::class.java)
        `when`(cases.findById(1)).thenReturn(Optional.of(CaseEntity(id=1, ownerSubject="u1")))
        assertThat(CaseService(cases, audits).get(1, CurrentUser("u1", setOf(Role.USER))))
            .isEqualTo(CaseResponse(1, "u1", CaseStatus.OPEN))
    }
}
