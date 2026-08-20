package com.training.case42.tasks

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
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
    fun `missing priority defaults to NORMAL`() {
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
}
