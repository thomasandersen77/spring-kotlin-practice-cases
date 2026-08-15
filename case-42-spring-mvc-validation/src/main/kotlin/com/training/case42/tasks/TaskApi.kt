package com.training.case42.tasks

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class TaskApplication

fun main(args: Array<String>) {
 runApplication<TaskApplication>(*args)
}

enum class TaskPriority { LOW, NORMAL, HIGH }

data class CreateTaskRequest(
 @field:NotBlank(message = "title kan ikke være blank")
 @field:Size(max = 80, message = "title kan ikke være lengre enn 80 tegn")
 val title: String,
 val priority: TaskPriority = TaskPriority.NORMAL
)

data class TaskResponse(val id: Long, val title: String, val priority: TaskPriority)

@Service
class TaskService {
 // TODO 1: Normaliser input, opprett oppgaven og returner response-DTO.
 fun create(request: CreateTaskRequest): TaskResponse = TODO("Implementer application service")
}

@RestController
@RequestMapping("/api/tasks")
class TaskController(private val service: TaskService) {
 @PostMapping
 fun create(@Valid @RequestBody request: CreateTaskRequest): ResponseEntity<TaskResponse> =
 ResponseEntity.status(HttpStatus.CREATED).body(service.create(request))
}
