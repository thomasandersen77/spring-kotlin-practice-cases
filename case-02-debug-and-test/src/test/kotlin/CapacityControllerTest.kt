import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@WebMvcTest(CapacityController::class)
@ContextConfiguration(classes = [CapacityController::class])
class CapacityControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `verify that the controller does http correct`() {
        val capacityRequest = CapacityRequest(
            from = LocalDate.of(2023, 1, 1),
            to = LocalDate.of(2023, 1, 7),
            absenceDates = emptySet())

        val response = mockMvc.perform(
            post("/capacity/available-days", "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(capacityRequest))
        )
            .andExpect(status().isOk)
            .andReturn().response

        val capacityResponse = objectMapper.readValue(
            response.contentAsString,
            CapacityResponse::class.java
        )
        assertNotNull(capacityResponse)
        assertEquals(5, capacityResponse.availableDays)

    }
}
