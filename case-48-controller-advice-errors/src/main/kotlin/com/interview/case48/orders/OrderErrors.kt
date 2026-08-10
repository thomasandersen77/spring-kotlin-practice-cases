package com.interview.case48.orders

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

@SpringBootApplication
class OrderErrorApplication

fun main(args: Array<String>) {
    runApplication<OrderErrorApplication>(*args)
}

data class CreateOrderRequest(
    @field:NotBlank(message = "customerId kan ikke være blank") val customerId: String,
    @field:Positive(message = "quantity må være større enn 0") val quantity: Int
)

data class OrderResponse(val id: Long, val customerId: String, val quantity: Int)
data class FieldViolation(val field: String, val message: String)
data class ApiError(val code: String, val message: String, val violations: List<FieldViolation> = emptyList())

class OrderNotFound(id: Long) : RuntimeException("Ordre $id finnes ikke")
class OrderConflict(message: String) : RuntimeException(message)

@Service
class OrderService {
    fun get(id: Long): OrderResponse = TODO("Hent ordre eller kast OrderNotFound")
    fun create(request: CreateOrderRequest): OrderResponse = TODO("Opprett ordre eller kast OrderConflict")
}

@RestController
@RequestMapping("/api/orders")
class OrderController(private val service: OrderService) {
    @GetMapping("/{id}") fun get(@PathVariable id: Long) = service.get(id)
    @PostMapping fun create(@Valid @RequestBody request: CreateOrderRequest) = service.create(request)
}

@RestControllerAdvice
class OrderErrorHandler {
    // TODO 1: Oversett ikke-funnet til stabil 404-feilmodell.
    @ExceptionHandler(OrderNotFound::class)
    fun notFound(exception: OrderNotFound): ResponseEntity<ApiError> =
        TODO("Map OrderNotFound")

    // TODO 2: Oversett konflikt til 409 uten å lekke intern exception-type.
    @ExceptionHandler(OrderConflict::class)
    fun conflict(exception: OrderConflict): ResponseEntity<ApiError> =
        TODO("Map OrderConflict")

    // TODO 3: Samle Bean Validation-feil deterministisk per felt.
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validation(exception: MethodArgumentNotValidException): ResponseEntity<ApiError> =
        TODO("Map valideringsfeil")
}
