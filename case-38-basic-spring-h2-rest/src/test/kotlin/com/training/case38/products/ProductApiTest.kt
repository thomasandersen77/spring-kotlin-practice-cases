package com.training.case38.products

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class ProductApiTest(@Autowired val mvc: MockMvc) {
	@Test
	fun `gyldig POST oppretter produkt og returnerer JSON`() {
		mvc.perform(
				post("/api/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""{"sku":"K-1","name":"Kotlin","initialStock":3}""")
			)
			.andExpect(status().isCreated)
			.andExpect(jsonPath("$.sku").value("K-1"))
	}

	@Test
	fun `ugyldig POST gir 400`() {
		mvc.perform(
				post("/api/products")
					.contentType(MediaType.APPLICATION_JSON)
					.content("""{"sku":"","name":"","initialStock":-1}""")
			)
			.andExpect(status().isBadRequest)
	}

	@Test
	fun `ukjent id gir 404`() {
		mvc.perform(get("/api/products/999")).andExpect(status().isNotFound)
	}
}
