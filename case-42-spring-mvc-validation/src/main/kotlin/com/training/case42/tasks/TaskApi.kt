package com.training.case42.tasks

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.concurrent.atomic.AtomicLong

@SpringBootApplication
class TaskApplication

fun main(args: Array<String>) {
    runApplication<TaskApplication>(*args)
}

enum class TaskPriority {
    LOW,
    NORMAL,
    HIGH,
}

const val TITLE_MAX_LENGTH_ERROR_MESSAGE = "title kan ikke være lengre enn 80 tegn"

data class CreateTaskRequest(
    @field:NotBlank(message = "title kan ikke være blank")
    @field:Size(max = 80, message = TITLE_MAX_LENGTH_ERROR_MESSAGE)
    val title: String,
    val priority: TaskPriority = TaskPriority.NORMAL,
)

data class TaskResponse(val id: Long, val title: String, val priority: TaskPriority)

@Service
class TaskService {
    // TODO 1: Normaliser input, opprett oppgaven og returner response-DTO.
    fun create(request: CreateTaskRequest): TaskResponse {
		val ids = AtomicLong()
        return TaskResponse(
            id = ids.incrementAndGet(),
            title = request.title.trim(),
            priority = request.priority
        )
    }
}

@RestController
@RequestMapping("/api/tasks")
class TaskController(private val service: TaskService) {
    @PostMapping
    fun create(@Valid @RequestBody request: CreateTaskRequest): ResponseEntity<TaskResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(service.create(request))
}

// Fast feilformat klienten kan parse på
data class ValidationError(val field: String, val message: String)

@RestControllerAdvice
class TaskControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)   // gir 400
    fun onValidation(ex: MethodArgumentNotValidException): List<ValidationError> =
        ex.bindingResult.fieldErrors.map {
            println(it)
            ValidationError(it.field, it.defaultMessage ?: "ugyldig verdi")
        }
}

