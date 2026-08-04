package com.interview.case36.bank.adapter.web

import com.interview.case36.bank.adapter.web.dto.ApiErrorResponse
import com.interview.case36.bank.domain.BankingException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

/**
 * Central error handling for the banking API. Known error types are mapped to stable, predictable
 * responses - no stack trace, database error or internal class name is ever leaked to the client.
 *
 * The two generic handlers below (Bean Validation and `IllegalArgumentException`, e.g. from
 * `Money`/`BankAccount` guard clauses) are already implemented - they are mechanical. TODO 14 is the
 * one that actually requires judgement: mapping each domain error code to the right HTTP status.
 */
@RestControllerAdvice
class BankingExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
        val firstError = ex.bindingResult.allErrors.firstOrNull()
        val message = if (firstError is FieldError) {
            "${firstError.field}: ${firstError.defaultMessage}"
        } else {
            ex.message ?: "Validation failed"
        }
        return ResponseEntity.badRequest().body(
            ApiErrorResponse(code = "VALIDATION_ERROR", message = message, timestamp = Instant.now())
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

    /**
     * TODO 14: Map [BankingException.code] to the correct [org.springframework.http.HttpStatus] and
     * build an [ApiErrorResponse].
     *
     * Expected status per code:
     * - `ACCOUNT_NOT_FOUND`   -> 404 Not Found
     * - `INVALID_TRANSFER`    -> 400 Bad Request
     * - `INSUFFICIENT_FUNDS`  -> 422 Unprocessable Entity
     * - `ACCOUNT_BLOCKED`     -> 409 Conflict
     */
    @ExceptionHandler(BankingException::class)
    fun handleBankingException(ex: BankingException): ResponseEntity<ApiErrorResponse> =
        TODO("TODO 14: map ex.code til riktig HttpStatus og bygg en ApiErrorResponse")
}
