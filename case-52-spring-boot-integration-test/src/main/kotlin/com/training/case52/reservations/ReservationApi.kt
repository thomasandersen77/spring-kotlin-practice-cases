package com.training.case52.reservations

import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI

data class CreateReservationRequest(
    @field:NotBlank val productCode: String,
    @field:Positive val quantity: Int,
    @field:NotBlank @field:Email val customerEmail: String
)

@RestController
@RequestMapping("/api/reservations")
class ReservationController(private val service: ReservationService) {
    @PostMapping
    fun create(@Valid @RequestBody request: CreateReservationRequest): ResponseEntity<ReservationResult> {
        val result = service.reserve(request.productCode, request.quantity, request.customerEmail)
        return ResponseEntity.created(URI.create("/api/reservations/${result.id}")).body(result)
    }

    @GetMapping("/{id}")
    fun find(@PathVariable id: Long): ReservationResult = service.find(id)
}

data class ApiError(val message: String)

@RestControllerAdvice
class ReservationExceptionHandler {
    @ExceptionHandler(ProductNotFound::class, ReservationNotFound::class)
    fun notFound(exception: RuntimeException): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError(requireNotNull(exception.message)))

    @ExceptionHandler(InsufficientStock::class)
    fun conflict(exception: InsufficientStock): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(ApiError(requireNotNull(exception.message)))
}

