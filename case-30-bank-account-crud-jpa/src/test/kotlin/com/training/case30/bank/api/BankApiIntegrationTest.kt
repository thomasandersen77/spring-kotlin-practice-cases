package com.training.case30.bank.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.training.case30.bank.persistence.SpringDataBankAccountRepository
import com.training.case30.bank.persistence.SpringDataCustomerRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class BankApiIntegrationTest {

 @Autowired
 lateinit var mockMvc: MockMvc

 @Autowired
 lateinit var objectMapper: ObjectMapper

 @Autowired
 lateinit var customerRepository: SpringDataCustomerRepository

 @Autowired
 lateinit var accountRepository: SpringDataBankAccountRepository

 @Test
 fun `post customers should return 201`() {
 val payload = mapOf("name" to "Ola Nordmann", "email" to "ola.api@example.no")

 mockMvc.perform(
 post("/api/customers")
 .contentType(MediaType.APPLICATION_JSON)
 .content(objectMapper.writeValueAsString(payload))
 )
 .andExpect(status().isCreated)
 .andExpect(jsonPath("$.id").exists())
 .andExpect(jsonPath("$.email").value("ola.api@example.no"))
 }

 @Test
 fun `invalid email should return 400`() {
 val payload = mapOf("name" to "Ola Nordmann", "email" to "not-an-email")

 mockMvc.perform(
 post("/api/customers")
 .contentType(MediaType.APPLICATION_JSON)
 .content(objectMapper.writeValueAsString(payload))
 )
 .andExpect(status().isBadRequest)
 }

 @Test
 fun `get unknown customer should return 404`() {
 mockMvc.perform(get("/api/customers/00000000-0000-0000-0000-000000000099"))
 .andExpect(status().isNotFound)
 }

 @Test
 fun `account can be created for existing customer`() {
 val customerId = createCustomer("Kari", "kari.api@example.no")
 val payload = mapOf(
 "customerId" to customerId,
 "accountNumber" to "15030077777",
 "displayName" to "Brukskonto"
 )

 mockMvc.perform(
 post("/api/accounts")
 .contentType(MediaType.APPLICATION_JSON)
 .content(objectMapper.writeValueAsString(payload))
 )
 .andExpect(status().isCreated)
 .andExpect(jsonPath("$.customerId").value(customerId))
 .andExpect(jsonPath("$.balance").value(0))
 }

 @Test
 fun `account cannot be created for unknown customer`() {
 val payload = mapOf(
 "customerId" to "00000000-0000-0000-0000-000000000123",
 "accountNumber" to "15030088888",
 "displayName" to "Brukskonto"
 )

 mockMvc.perform(
 post("/api/accounts")
 .contentType(MediaType.APPLICATION_JSON)
 .content(objectMapper.writeValueAsString(payload))
 )
 .andExpect(status().isNotFound)
 }

 @Test
 fun `deposit should return updated balance`() {
 val customerId = createCustomer("Per", "per.api@example.no")
 val accountId = createAccount(customerId, "15030099999", "Lønn")

 mockMvc.perform(
 post("/api/accounts/$accountId/deposits")
 .contentType(MediaType.APPLICATION_JSON)
 .content("""{"amount":1000.00}""")
 )
 .andExpect(status().isOk)
 .andExpect(jsonPath("$.balance").value(1000.0))
 }

 @Test
 fun `withdraw with insufficient balance should return 409`() {
 val customerId = createCustomer("Lise", "lise.api@example.no")
 val accountId = createAccount(customerId, "15030100000", "Sparekonto")

 mockMvc.perform(
 post("/api/accounts/$accountId/withdrawals")
 .contentType(MediaType.APPLICATION_JSON)
 .content("""{"amount":250.00}""")
 )
 .andExpect(status().isConflict)
 }

 @Test
 fun `deleting account with balance should return 409`() {
 val customerId = createCustomer("Mona", "mona.api@example.no")
 val accountId = createAccount(customerId, "15030111111", "Sparekonto")
 mockMvc.perform(
 post("/api/accounts/$accountId/deposits")
 .contentType(MediaType.APPLICATION_JSON)
 .content("""{"amount":100.00}""")
 )

 mockMvc.perform(delete("/api/accounts/$accountId"))
 .andExpect(status().isConflict)
 }

 @Test
 fun `account put should not allow balance update`() {
 val customerId = createCustomer("Nina", "nina.api@example.no")
 val accountId = createAccount(customerId, "15030122222", "Regningskonto")

 mockMvc.perform(
 put("/api/accounts/$accountId")
 .contentType(MediaType.APPLICATION_JSON)
 .content("""{"displayName":"Nytt navn","balance":999999.99}""")
 )
 .andExpect(status().isOk)
 .andExpect(jsonPath("$.displayName").value("Nytt navn"))
 .andExpect(jsonPath("$.balance").value(0))
 }

 @Test
 fun `customer without accounts should support full crud flow`() {
 val createdCustomerId = createCustomer("Rune", "rune.api@example.no")

 mockMvc.perform(get("/api/customers/$createdCustomerId"))
 .andExpect(status().isOk)

 mockMvc.perform(
 put("/api/customers/$createdCustomerId")
 .contentType(MediaType.APPLICATION_JSON)
 .content("""{"name":"Rune Oppdatert","email":"rune.oppdatert@example.no"}""")
 )
 .andExpect(status().isOk)
 .andExpect(jsonPath("$.name").value("Rune Oppdatert"))

 mockMvc.perform(delete("/api/customers/$createdCustomerId"))
 .andExpect(status().isNoContent)

 mockMvc.perform(get("/api/customers/$createdCustomerId"))
 .andExpect(status().isNotFound)
 }

 private fun createCustomer(name: String, email: String): String {
 val response = mockMvc.perform(
 post("/api/customers")
 .contentType(MediaType.APPLICATION_JSON)
 .content("""{"name":"$name","email":"$email"}""")
 ).andReturn().response.contentAsString

 return objectMapper.readTree(response).get("id").asText()
 }

 private fun createAccount(customerId: String, accountNumber: String, displayName: String): String {
 val response = mockMvc.perform(
 post("/api/accounts")
 .contentType(MediaType.APPLICATION_JSON)
 .content(
 """{"customerId":"$customerId","accountNumber":"$accountNumber","displayName":"$displayName"}"""
 )
 ).andReturn().response.contentAsString

 val id = objectMapper.readTree(response).get("id").asText()
 assertThat(id).isNotBlank()
 return id
 }
}
