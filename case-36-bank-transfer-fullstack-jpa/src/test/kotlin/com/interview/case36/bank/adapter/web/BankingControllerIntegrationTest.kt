package com.interview.case36.bank.adapter.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.interview.case36.bank.application.port.AccountRepository
import com.interview.case36.bank.domain.AccountId
import com.interview.case36.bank.support.TestDataFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

/**
 * End-to-end tests through the real HTTP stack (MockMvc -> controller -> service -> domain -> JPA ->
 * H2). Account setup for the "blocked account" scenario goes through [accountRepository] directly
 * (there is no HTTP endpoint to block an account in this case) - everything that is actually under
 * test (the transfer request itself) still goes through MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BankingControllerIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Test
    fun `opprett konto gir 201`() {
        mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"ownerName":"Kari Nordmann"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.accountId").exists())
            .andExpect(jsonPath("$.ownerName").value("Kari Nordmann"))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.balance").value(0))
    }

    @Test
    fun `hent konto gir 200 og korrekt DTO`() {
        val accountId = createAccountViaApi("Ola Hansen")

        mockMvc.perform(get("/api/accounts/$accountId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accountId").value(accountId))
            .andExpect(jsonPath("$.ownerName").value("Ola Hansen"))
    }

    @Test
    fun `ukjent konto gir 404`() {
        mockMvc.perform(get("/api/accounts/00000000-0000-0000-0000-000000000099"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `innskudd gir oppdatert saldo`() {
        val accountId = createAccountViaApi("Liv Berg")

        mockMvc.perform(
            post("/api/accounts/$accountId/deposits")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"amount":500.00}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.balance").value(500.00))
    }

    @Test
    fun `overforing gir korrekt TransferResponse`() {
        val fromId = createAccountViaApi("Per Ås")
        val toId = createAccountViaApi("Mona Li")
        deposit(fromId, "1000.00")

        mockMvc.perform(
            post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"fromAccountId":"$fromId","toAccountId":"$toId","amount":250.00}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.transferId").exists())
            .andExpect(jsonPath("$.fromAccountId").value(fromId))
            .andExpect(jsonPath("$.toAccountId").value(toId))
            .andExpect(jsonPath("$.amount").value(250.00))
    }

    @Test
    fun `ugyldig belop gir 400`() {
        val fromId = createAccountViaApi("Nina Dahl")
        val toId = createAccountViaApi("Rune Iversen")

        mockMvc.perform(
            post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"fromAccountId":"$fromId","toAccountId":"$toId","amount":-50.00}""")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `overforing til samme konto gir 400`() {
        val accountId = createAccountViaApi("Silje Aas")

        mockMvc.perform(
            post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"fromAccountId":"$accountId","toAccountId":"$accountId","amount":10.00}""")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `manglende dekning gir 422`() {
        val fromId = createAccountViaApi("Tor Vik")
        val toId = createAccountViaApi("Ingrid Solheim")

        mockMvc.perform(
            post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"fromAccountId":"$fromId","toAccountId":"$toId","amount":100.00}""")
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `blokkert avsenderkonto gir 409`() {
        val blocked = TestDataFactory.blockedAccount(ownerName = "Blokkert Bruker", balanceKroner = "1000.00")
        accountRepository.save(blocked)
        val toId = createAccountViaApi("Mottaker")

        mockMvc.perform(
            post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"fromAccountId":"${blocked.id.value}","toAccountId":"$toId","amount":10.00}""")
        )
            .andExpect(status().isConflict)
    }

    @Test
    fun `JSON-responsen inneholder ikke JPA-felter eller intern versjon`() {
        val accountId = createAccountViaApi("Frode Lien")

        mockMvc.perform(get("/api/accounts/$accountId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.version").doesNotExist())
    }

    @Test
    fun `vellykket HTTP-overforing er faktisk persistert i H2`() {
        val fromId = createAccountViaApi("Hedda Moe")
        val toId = createAccountViaApi("Sindre Berg")
        deposit(fromId, "800.00")

        mockMvc.perform(
            post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"fromAccountId":"$fromId","toAccountId":"$toId","amount":300.00}""")
        )
            .andExpect(status().isOk)

        val fromAccount = accountRepository.findById(AccountId(UUID.fromString(fromId)))
            ?: error("expected sender account to exist")
        val toAccount = accountRepository.findById(AccountId(UUID.fromString(toId)))
            ?: error("expected recipient account to exist")

        assertThat(fromAccount.balance.toKroner()).isEqualByComparingTo("500.00")
        assertThat(toAccount.balance.toKroner()).isEqualByComparingTo("300.00")
    }

    private fun createAccountViaApi(ownerName: String): String {
        val response = mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"ownerName":"$ownerName"}""")
        ).andReturn().response.contentAsString

        return objectMapper.readTree(response).get("accountId").asText()
    }

    private fun deposit(accountId: String, amountKroner: String) {
        mockMvc.perform(
            post("/api/accounts/$accountId/deposits")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"amount":$amountKroner}""")
        ).andExpect(status().isOk)
    }
}
