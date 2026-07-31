package com.interview.case30.bank.api

import com.interview.case30.bank.domain.AccountStatus
import com.interview.case30.bank.domain.BankAccount
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.util.UUID

data class CreateAccountRequest(
    @field:NotNull
    val customerId: UUID?,
    @field:NotBlank
    val accountNumber: String,
    @field:NotBlank
    val displayName: String
)

data class UpdateAccountRequest(
    @field:NotBlank
    val displayName: String
)

data class AmountRequest(
    @field:NotNull
    @field:DecimalMin(value = "0.01", inclusive = true)
    val amount: BigDecimal?
)

data class AccountResponse(
    val id: UUID,
    val accountNumber: String,
    val customerId: UUID,
    val displayName: String,
    val balance: BigDecimal,
    val currency: String = "NOK",
    val status: AccountStatus
)

fun BankAccount.toResponse(): AccountResponse = AccountResponse(
    id = id.value,
    accountNumber = accountNumber.value,
    customerId = customerId.value,
    displayName = displayName,
    balance = balance.amount,
    status = status
)
