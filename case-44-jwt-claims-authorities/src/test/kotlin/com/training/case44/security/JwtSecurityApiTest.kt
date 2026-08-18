package com.training.case44.security

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class JwtSecurityApiTest(@Autowired private val mvc: MockMvc) {

	@MockBean private lateinit var jwtDecoder: JwtDecoder

	@Test
	fun `manglende token gir 401`() {
		mvc.get("/api/invoices").andExpect { status { isUnauthorized() } }
	}

	@Test
	fun `token uten lesetilgang gir 403`() {
		mvc.get("/api/invoices") {
				with(jwt())
			}
			.andExpect { status { isForbidden() } }
	}

	@Test
	fun `read scope gir 200`() {
		mvc.get("/api/invoices") {
				with(jwt().authorities(SimpleGrantedAuthority("SCOPE_invoices:read")))
			}
			.andExpect { status { isOk() } }
	}
}
