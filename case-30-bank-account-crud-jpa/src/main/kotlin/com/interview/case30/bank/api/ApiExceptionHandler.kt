package com.interview.case30.bank.api

import com.interview.case30.bank.domain.DomainException
import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

data class ApiErrorResponse(
    val code: String,
    val message: String,
    val timestamp: Instant
)

@RestControllerAdvice
class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
        val firstError = ex.bindingResult.allErrors.firstOrNull()
        val message = if (firstError is FieldError) {
            "${firstError.field}: ${firstError.defaultMessage}"
        } else {
            ex.message ?: "Validation failed"
        }
        return ResponseEntity.badRequest().body(
            ApiErrorResponse(
                code = "VALIDATION_ERROR",
                message = message,
                timestamp = Instant.now()
            )
        )
    }

    @ExceptionHandler(ConstraintViolationException::class, IllegalArgumentException::class)
    fun handleBadRequest(ex: Exception): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.badRequest().body(
            ApiErrorResponse(
                code = "BAD_REQUEST",
                message = ex.message ?: "Bad request",
                timestamp = Instant.now()
            )
        )

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleConflict(ex: DataIntegrityViolationException): ResponseEntity<ApiErrorResponse> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiErrorResponse(
                code = "CONFLICT",
                message = "Operation violated a data integrity constraint",
                timestamp = Instant.now()
            )
        )

    @ExceptionHandler(DomainException::class)
    fun handleDomain(ex: DomainException): ResponseEntity<ApiErrorResponse> {
        val status = when (ex.code) {
            "CUSTOMER_NOT_FOUND", "ACCOUNT_NOT_FOUND" -> HttpStatus.NOT_FOUND
            "INSUFFICIENT_FUNDS", "ACCOUNT_HAS_BALANCE", "CUSTOMER_HAS_ACCOUNTS",
            "EMAIL_ALREADY_IN_USE", "ACCOUNT_NUMBER_ALREADY_EXISTS", "ACCOUNT_CLOSED" -> HttpStatus.CONFLICT
            else -> HttpStatus.BAD_REQUEST
        }
        return ResponseEntity.status(status).body(
            ApiErrorResponse(
                code = ex.code,
                message = ex.message,
                timestamp = Instant.now()
            )
        )
    }
}
