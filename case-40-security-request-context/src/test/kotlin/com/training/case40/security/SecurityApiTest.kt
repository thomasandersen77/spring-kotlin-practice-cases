package com.training.case40.security

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class SecurityApiTest(@Autowired val mvc: MockMvc, @Autowired val cases: CaseRepository) {
	@MockBean lateinit var jwtDecoder: JwtDecoder
	private var caseId: Long = 0

	@BeforeEach
	fun seed() {
		cases.deleteAll()
		caseId = cases.save(CaseEntity(ownerSubject = "u1")).id!!
	}

	@Test
	fun `manglende token gir 401`() {
		mvc.perform(get("/api/cases/$caseId")).andExpect(status().isUnauthorized)
	}

	@Test
	fun `feil rolle gir 403`() {
		mvc.perform(
				post("/api/cases/$caseId/approve")
					.with(
						jwt()
							.jwt { it.subject("u1").claim("roles", listOf("USER")) }
							.authorities(SimpleGrantedAuthority("ROLE_USER"))
					)
			)
			.andExpect(status().isForbidden)
	}

	@Test
	fun `bruker kan lese egen ressurs`() {
		mvc.perform(
				get("/api/cases/$caseId")
					.with(
						jwt()
							.jwt { it.subject("u1").claim("roles", listOf("USER")) }
							.authorities(SimpleGrantedAuthority("ROLE_USER"))
					)
			)
			.andExpect(status().isOk)
	}

	@Test
	fun `bruker kan ikke lese en annens ressurs`() {
		mvc.perform(
				get("/api/cases/$caseId")
					.with(
						jwt()
							.jwt { it.subject("u2").claim("roles", listOf("USER")) }
							.authorities(SimpleGrantedAuthority("ROLE_USER"))
					)
			)
			.andExpect(status().isForbidden)
	}

	@Test
	fun `manager passerer sikkerhetsgrensen`() {
		mvc.perform(
				post("/api/cases/$caseId/approve")
					.with(
						jwt()
							.jwt { it.subject("m1").claim("roles", listOf("MANAGER")) }
							.authorities(SimpleGrantedAuthority("ROLE_MANAGER"))
					)
			)
			.andExpect(status().is2xxSuccessful)
	}
}
