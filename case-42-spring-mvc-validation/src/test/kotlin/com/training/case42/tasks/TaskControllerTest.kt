package com.training.case42.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
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

    val mapper = ObjectMapper().writerWithDefaultPrettyPrinter()

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
                jsonPath("$[0].field") { value("title") }
                jsonPath("$[0].message") { value("title kan ikke være blank") }
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
                jsonPath("$.message") { value("ugyldig verdi") }
            }

        verifyNoInteractions(service)
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

        verifyNoInteractions(service)
    }

    @Test
    fun `80 chars title gives 201 created`() {
        val request = CreateTaskRequest(
            title = "a".repeat(80),
            priority = TaskPriority.HIGH
        )

        `when`(service.create(
            request = request
        )).thenReturn(TaskResponse(
            1L,
            title = "a".repeat(80),
            priority = TaskPriority.HIGH
        ))


        val req = mapper.writeValueAsString(request)
        mvc.post("/api/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = req
        }
            .andExpect {
                status { isCreated() }
            }

        verify(service).create(request)
    }

}
