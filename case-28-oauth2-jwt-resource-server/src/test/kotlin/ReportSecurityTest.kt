package com.interview.reports

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Disse testene beskriver kontrakten for sikkerhetskonfigurasjonen.
 * Flere FEILER med dagens permitAll-konfigurasjon — det er selve oppgaven.
 */
@SpringBootTest(classes = [ReportApp::class])
@AutoConfigureMockMvc
class ReportSecurityTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var jwtDecoder: JwtDecoder // mockes bort: ingen ekte IdP i test

    @Test
    fun `public ping is open without token`() {
        mockMvc.perform(get("/public/ping"))
            .andExpect(status().isOk)
    }

    @Test
    fun `reports requires authentication`() {
        mockMvc.perform(get("/api/reports"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `reports rejects token without required scope`() {
        mockMvc.perform(
            get("/api/reports")
                .with(jwt().authorities(SimpleGrantedAuthority("SCOPE_other:scope")))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `reports accepts token with reports read scope`() {
        mockMvc.perform(
            get("/api/reports")
                .with(jwt().authorities(SimpleGrantedAuthority("SCOPE_reports:read")))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `delete requires admin role`() {
        mockMvc.perform(
            delete("/api/reports/R1")
                .with(jwt().authorities(SimpleGrantedAuthority("SCOPE_reports:read")))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `delete allowed for admin role`() {
        mockMvc.perform(
            delete("/api/reports/R1")
                .with(jwt().authorities(SimpleGrantedAuthority("ROLE_ADMIN")))
        )
            .andExpect(status().isNoContent)
    }

    // TODO: skriv en test som bruker jwt().jwt { it.claim("roles", listOf("ADMIN")) }
    //       og verifiser at din JwtAuthenticationConverter mapper claimet til ROLE_ADMIN
}
