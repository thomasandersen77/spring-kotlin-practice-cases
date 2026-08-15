package com.training.case44.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

class JwtAuthorityMapperTest {

 @Test
 fun `permissions og roller blir authorities med riktig prefiks`() {
 val jwt = Jwt(
 "token",
 Instant.now(),
 Instant.now().plusSeconds(60),
 mapOf("alg" to "none"),
 mapOf(
 "sub" to "user-1",
 "permissions" to listOf("invoices:read", "invoices:write"),
 "roles" to listOf("FINANCE_ADMIN")
 )
 )

 assertThat(JwtAuthorityMapper().convert(jwt).map { it.authority })
 .containsExactlyInAnyOrder(
 "SCOPE_invoices:read",
 "SCOPE_invoices:write",
 "ROLE_FINANCE_ADMIN"
 )
 }
}
