package com.training.case50.tasks

import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

data class CreateTaskRequest(val title: String)

data class TaskResponse(val id: Long, val title: String)

interface TaskService {
	suspend fun get(id: Long): TaskResponse?

	suspend fun create(request: CreateTaskRequest): TaskResponse
}

fun Application.taskApi(service: TaskService) {
	install(ContentNegotiation) {
		jackson()
	}

	routing {
		get("/tasks/{id}") {
			// TODO 1: Parse id, kall suspend-service og returner 200 eller 404.
			TODO("Implementer GET-routing")
		}
		post("/tasks") {
			// TODO 2: Deserialiser request, valider title og returner 201 eller 400.
			TODO("Implementer POST-routing")
		}
	}
}
