package com.interview.case52.reservations

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

// TODO: Skriv Spring Boot-integrasjonstestene beskrevet i README.md.
@SpringBootTest
@AutoConfigureMockMvc
class ReservationIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var reservationService: ReservationService

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var reservationRepository: ReservationRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        productRepository.save(ProductEntity(1L, "KOTLIN-21", 100))
    }

    @Test
    fun name() {

        val request = CreateReservationRequest(
            productCode = "KOTLIN-21",
            customerEmail = "thomas@gmail.com",
            quantity = 10
        )

        val json = objectMapper.writeValueAsString(request)

        val response = mockMvc.perform(
            post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
        ).andReturn().response

        Assertions.assertEquals(201, response.status)
        val result = objectMapper.readValue(response.contentAsString, ReservationResult::class.java)



    }
}