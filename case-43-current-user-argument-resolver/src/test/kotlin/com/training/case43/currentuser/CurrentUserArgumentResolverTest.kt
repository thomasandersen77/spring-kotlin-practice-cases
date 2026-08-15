package com.training.case43.currentuser

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.MethodParameter
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.context.request.ServletWebRequest
import java.time.Instant

class CurrentUserArgumentResolverTest {

 private val resolver = CurrentUserArgumentResolver()

 @Test
 fun `resolver stotter bare annotert AuthenticatedUser`() {
 assertThat(resolver.supportsParameter(parameter("annotated"))).isTrue()
 assertThat(resolver.supportsParameter(parameter("plain"))).isFalse()
 assertThat(resolver.supportsParameter(parameter("wrongType"))).isFalse()
 }

 @Test
 fun `jwt claims mappes til applikasjonsvennlig current user`() {
 val jwt = Jwt(
 "token",
 Instant.now(),
 Instant.now().plusSeconds(60),
 mapOf("alg" to "none"),
 mapOf("sub" to "user-7", "tenant_id" to "tenant-a", "roles" to listOf("USER"))
 )
 val request = MockHttpServletRequest().apply {
 userPrincipal = JwtAuthenticationToken(jwt)
 }

 val resolved = resolver.resolveArgument(
 parameter("annotated"),
 null,
 ServletWebRequest(request),
 null
 )

 assertThat(resolved).isEqualTo(AuthenticatedUser("user-7", "tenant-a", setOf("USER")))
 }

 private fun parameter(name: String): MethodParameter =
 MethodParameter(SampleController::class.java.getDeclaredMethod(name, parameterType(name)), 0)

 private fun parameterType(name: String): Class<*> =
 if (name == "wrongType") String::class.java else AuthenticatedUser::class.java

 private class SampleController {
 fun annotated(@CurrentUser user: AuthenticatedUser) = user
 fun plain(user: AuthenticatedUser) = user
 fun wrongType(@CurrentUser value: String) = value
 }
}
