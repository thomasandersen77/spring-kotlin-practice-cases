package com.interview.case50.tasks

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TaskApiTest {

    private val service = object : TaskService {
        override suspend fun get(id: Long): TaskResponse? =
            if (id == 7L) TaskResponse(7, "Lær Ktor") else null

        override suspend fun create(request: CreateTaskRequest) = TaskResponse(8, request.title)
    }

    @Test
    fun `GET returnerer oppgave eller 404`() = testApplication {
        application { taskApi(service) }

        assertThat(client.get("/tasks/7").status).isEqualTo(HttpStatusCode.OK)
        assertThat(client.get("/tasks/99").status).isEqualTo(HttpStatusCode.NotFound)
    }

    @Test
    fun `POST deserialiserer request og returnerer 201`() = testApplication {
        application { taskApi(service) }

        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"title":"Lær Ktor"}""")
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
    }
}
