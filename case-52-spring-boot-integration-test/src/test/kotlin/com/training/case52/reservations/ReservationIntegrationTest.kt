package com.training.case52.reservations

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class ReservationIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var reservationRepository: ReservationRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    private val productCode = "KOTLIN-21"
    private val email = "thomas@gmail.com"

    @BeforeEach
    fun setUp() {
        reservationRepository.deleteAll()
        productRepository.deleteAll()
    }

    @Test
    fun `should create reservation successfully`() {
        // 1. Lagre et produkt med productCode = "KOTLIN-21" og beholdning 10.
        productRepository.save(ProductEntity(productCode = productCode, stock = 10))

        // 2. Send POST /api/reservations med antall 3 og en gyldig e-postadresse.
        val request = CreateReservationRequest(
            productCode = productCode,
            customerEmail = email,
            quantity = 3
        )

        mockMvc.post("/api/reservations") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            // 3. Verifiser 201 Created.
            status { isCreated() }
            // 4. Verifiser responsens sentrale JSON-felter.
            jsonPath("$.productCode") { value(productCode) }
            jsonPath("$.customerEmail") { value(email) }
            jsonPath("$.quantity") { value(3) }
            jsonPath("$.remainingStock") { value(7) }
        }

        // 5. Les produktet på nytt fra repository og verifiser beholdning 7.
        val product = productRepository.findByProductCode(productCode)
        assertThat(product?.stock).isEqualTo(7)

        // 6. Verifiser at én reservasjon er lagret med riktige verdier.
        val reservations = reservationRepository.findAll()
        assertThat(reservations).hasSize(1)
        with(reservations[0]) {
            assertThat(this.productCode).isEqualTo(this@ReservationIntegrationTest.productCode)
            assertThat(this.quantity).isEqualTo(3)
            assertThat(this.customerEmail).isEqualTo(email)
        }
    }

    @Test
    fun `should fail when stock is insufficient`() {
        // 1. Lagre et produkt med beholdning 2.
        productRepository.save(ProductEntity(productCode = productCode, stock = 2))

        // 2. Forsøk å reservere 3.
        val request = CreateReservationRequest(
            productCode = productCode,
            customerEmail = email,
            quantity = 3
        )

        mockMvc.post("/api/reservations") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            // 3. Verifiser 409 Conflict.
            status { isConflict() }
        }

        // 4. Verifiser at ingen reservasjon er lagret.
        assertThat(reservationRepository.findAll()).isEmpty()

        // 5. Les produktet på nytt og verifiser at beholdningen fremdeles er 2.
        val product = productRepository.findByProductCode(productCode)
        assertThat(product?.stock).isEqualTo(2)
    }

    @Test
    fun `should fail when request is invalid`() {
        // 1. Lagre et produkt med beholdning 10.
        productRepository.save(ProductEntity(productCode = productCode, stock = 10))

        // 2. Send en request med quantity = 0.
        val request = CreateReservationRequest(
            productCode = productCode,
            customerEmail = email,
            quantity = 0
        )

        mockMvc.post("/api/reservations") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            // 3. Verifiser 400 Bad Request.
            status { isBadRequest() }
        }

        // 4. Verifiser at databasen ikke er endret.
        assertThat(reservationRepository.findAll()).isEmpty()
        val product = productRepository.findByProductCode(productCode)
        assertThat(product?.stock).isEqualTo(10)
    }
}
