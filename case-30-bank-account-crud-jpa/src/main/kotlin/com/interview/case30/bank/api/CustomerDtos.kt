package com.interview.case30.bank.api

import com.interview.case30.bank.domain.Customer
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class CreateCustomerRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    @field:Email
    val email: String
)

data class UpdateCustomerRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    @field:Email
    val email: String
)

data class CustomerResponse(
    val id: UUID,
    val name: String,
    val email: String
)

fun Customer.toResponse(): CustomerResponse = CustomerResponse(
    id = id.value,
    name = name,
    email = email
)
