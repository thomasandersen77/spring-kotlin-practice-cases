package com.interview.case46.messages

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(MessageController::class)
@Import(MessageSecurityConfig::class)
class MessageSecurityTest(@Autowired private val mvc: MockMvc) {

    @MockBean private lateinit var service: MessageService
    @MockBean private lateinit var jwtDecoder: JwtDecoder

    @Test
    fun `uten token gir 401`() {
        TODO("Bruk MockMvc og verifiser 401")
    }

    @Test
    fun `read scope gir 200 pa GET`() {
        TODO("Bruk mock JWT med SCOPE_messages:read")
    }

    @Test
    fun `read scope alene gir 403 pa POST`() {
        TODO("Skill autentisert fra autorisert bruker")
    }

    @Test
    fun `write scope gir 201 og delegasjon til service`() {
        TODO("Isoler controller og service uten å overteste Spring Security")
    }
}
