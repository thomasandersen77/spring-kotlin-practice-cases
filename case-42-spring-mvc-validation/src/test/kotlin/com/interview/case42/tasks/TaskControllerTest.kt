package com.interview.case42.tasks

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(TaskController::class)
class TaskControllerTest(@Autowired private val mvc: MockMvc) {

    @MockBean
    private lateinit var service: TaskService

    @Test
    fun `gyldig request gir 201 og response dto`() {
        `when`(service.create(CreateTaskRequest("Forbered intervju", TaskPriority.HIGH)))
            .thenReturn(TaskResponse(7, "Forbered intervju", TaskPriority.HIGH))

        mvc.post("/api/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"Forbered intervju","priority":"HIGH"}"""
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { value(7) }
            jsonPath("$.priority") { value("HIGH") }
        }
    }

    @Test
    fun `blank tittel gir 400 uten servicekall`() {
        mvc.post("/api/tasks") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"  ","priority":"NORMAL"}"""
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
