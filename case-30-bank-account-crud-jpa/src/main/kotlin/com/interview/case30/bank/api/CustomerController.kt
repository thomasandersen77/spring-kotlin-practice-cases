package com.interview.case30.bank.api

import com.interview.case30.bank.application.CustomerService
import com.interview.case30.bank.domain.CustomerId
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/customers")
class CustomerController(
    private val customerService: CustomerService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateCustomerRequest): CustomerResponse =
        customerService.create(request.name, request.email).toResponse()

    @GetMapping("/{customerId}")
    fun get(@PathVariable customerId: UUID): CustomerResponse =
        customerService.get(CustomerId(customerId)).toResponse()

    @GetMapping
    fun list(): List<CustomerResponse> =
        customerService.list().map { it.toResponse() }

    @PutMapping("/{customerId}")
    fun update(
        @PathVariable customerId: UUID,
        @Valid @RequestBody request: UpdateCustomerRequest
    ): CustomerResponse =
        customerService.update(CustomerId(customerId), request.name, request.email).toResponse()

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable customerId: UUID) {
        customerService.delete(CustomerId(customerId))
    }
}
