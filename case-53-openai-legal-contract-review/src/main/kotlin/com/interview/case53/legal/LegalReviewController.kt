package com.interview.case53.legal

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ReviewContractHttpRequest(@field:NotBlank val clauseText: String)

@RestController
@RequestMapping("/api/legal-reviews")
class LegalReviewController(private val service: LegalReviewService) {
    @PostMapping
    fun review(@Valid @RequestBody request: ReviewContractHttpRequest): ContractReview =
        service.review(request.clauseText)
}

@RestControllerAdvice
class LegalReviewErrorHandler {
    @ExceptionHandler(OpenAiUnavailable::class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    fun unavailable(ex: OpenAiUnavailable) = mapOf("error" to ex.message)

    @ExceptionHandler(InvalidModelResponse::class, ModelRefused::class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    fun invalid(ex: RuntimeException) = mapOf("error" to ex.message)
}
