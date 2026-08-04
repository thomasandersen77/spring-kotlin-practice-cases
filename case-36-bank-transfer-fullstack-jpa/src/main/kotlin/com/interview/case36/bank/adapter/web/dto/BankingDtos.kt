package com.interview.case36.bank.adapter.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * These DTOs are the API contract. Bean Validation here is limited to simple, syntactic checks
 * (blank name, missing amount/ids) - business rules such as "amount must be positive" or "not the same
 * account" live in the domain/application layer and are surfaced through
 * [com.interview.case36.bank.adapter.web.BankingExceptionHandler] instead.
 */

data class CreateAccountRequest(
    @field:NotBlank
    val ownerName: String
)

data class DepositRequest(
    @field:NotNull
    val amount: BigDecimal?
)

data class TransferMoneyRequest(
    @field:NotNull
    val fromAccountId: UUID?,
    @field:NotNull
    val toAccountId: UUID?,
    @field:NotNull
    val amount: BigDecimal?
)

data class AccountResponse(
    val accountId: UUID,
    val ownerName: String,
    val status: String,
    val balance: BigDecimal
)

data class TransferResponse(
    val transferId: UUID,
    val fromAccountId: UUID,
    val toAccountId: UUID,
    val amount: BigDecimal,
    val executedAt: Instant
)

data class ApiErrorResponse(
    val code: String,
    val message: String,
    val timestamp: Instant
)
