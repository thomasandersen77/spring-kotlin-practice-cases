package com.training.case42.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(TaskController::class)
class TaskControllerTest(
    @Autowired private val mvc: MockMvc
) {

    @MockBean
    private lateinit var service: TaskService

    var mapper = ObjectMapper().writerWithDefaultPrettyPrinter()

    @Test
    fun `gyldig request gir 201 og response dto`() {
        `when`(service.create(CreateTaskRequest("Forbered trening", TaskPriority.HIGH)))
            .thenReturn(TaskResponse(7, "Forbered trening", TaskPriority.HIGH))

        mvc.post("/api/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"Forbered trening","priority":"HIGH"}"""
        }
            .andExpect {
                status { isCreated() }
                jsonPath("$.id") { value(7) }
                jsonPath("$.priority") { value("HIGH") }
            }
    }

    @Test
    fun `blank tittel gir 400 uten servicekall`() {
        mvc.post("/api/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":" ","priority":"NORMAL"}"""
        }
            .andExpect {
                status { isBadRequest() }
            }
    }

    @Test
    fun `manglende satt priority defaulter til NORMAL`() {
        `when`(service.create(CreateTaskRequest("Hello World")))
            .thenReturn(TaskResponse(8, "Hello World", TaskPriority.NORMAL))

        mvc.post("/api/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"Hello World"}"""
        }
            .andExpect {
                status { isCreated() }
				jsonPath("$.title") { value("Hello World") }
                jsonPath("$.priority") { value("NORMAL") }
            }

        verify(service).create(CreateTaskRequest("Hello World"))
    }

    @Test
    fun `too long title gives bad request`() {

        `when`(service.create(CreateTaskRequest("a".repeat(81), TaskPriority.HIGH)))
            .thenReturn(TaskResponse(7, "Forbered trening", TaskPriority.HIGH))

        val request = CreateTaskRequest(
            title = "a".repeat(81),
            priority = TaskPriority.HIGH
        )

        val req = mapper.writeValueAsString(request)
        mvc.post("/api/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = req
        }
            .andExpect {
                status { isBadRequest() }
            }

        verify(service, times(0)).create(request)
    }

}
