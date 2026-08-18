package com.training.case48.orders

import org.junit.jupiter.api.Test
import org.mockito.Mockito.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(OrderController::class, OrderErrorHandler::class)
class OrderErrorHandlerTest(@Autowired private val mvc: MockMvc) {

	@MockBean private lateinit var service: OrderService

	@Test
	fun `ukjent ordre gir 404 med stabil feilkode`() {
		doThrow(OrderNotFound(99)).`when`(service).get(99)

		mvc.get("/api/orders/99").andExpect {
			status { isNotFound() }
			jsonPath("$.code") { value("ORDER_NOT_FOUND") }
			jsonPath("$.message") { value("Ordre 99 finnes ikke") }
		}
	}

	@Test
	fun `ugyldig request gir 400 med feltfeil`() {
		mvc.post("/api/orders") {
				contentType = MediaType.APPLICATION_JSON
				content = """{"customerId":" ","quantity":0}"""
			}
			.andExpect {
				status { isBadRequest() }
				jsonPath("$.code") { value("VALIDATION_ERROR") }
				jsonPath("$.violations.length()") { value(2) }
			}
	}

	@Test
	fun `forretningskonflikt gir 409 med stabil feilkode`() {
		val request = CreateOrderRequest("customer-1", 2)
		doThrow(OrderConflict("Ordren finnes allerede")).`when`(service).create(request)

		mvc.post("/api/orders") {
				contentType = MediaType.APPLICATION_JSON
				content = """{"customerId":"customer-1","quantity":2}"""
			}
			.andExpect {
				status { isConflict() }
				jsonPath("$.code") { value("ORDER_CONFLICT") }
			}
	}
}
