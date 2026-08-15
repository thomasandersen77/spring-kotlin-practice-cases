package com.training.case30.bank.api

import com.training.case30.bank.application.BankAccountService
import com.training.case30.bank.domain.AccountId
import com.training.case30.bank.domain.AccountNumber
import com.training.case30.bank.domain.CustomerId
import com.training.case30.bank.domain.Money
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/accounts")
class BankAccountController(
 private val bankAccountService: BankAccountService
) {
 @PostMapping
 @ResponseStatus(HttpStatus.CREATED)
 fun create(@Valid @RequestBody request: CreateAccountRequest): AccountResponse {
 val customerId = request.customerId ?: throw IllegalArgumentException("customerId is required")
 return bankAccountService.create(
 customerId = CustomerId(customerId),
 accountNumber = AccountNumber(request.accountNumber),
 displayName = request.displayName
 ).toResponse()
 }

 @GetMapping("/{accountId}")
 fun get(@PathVariable accountId: UUID): AccountResponse =
 bankAccountService.get(AccountId(accountId)).toResponse()

 @GetMapping
 fun list(@RequestParam(required = false) customerId: UUID?): List<AccountResponse> =
 bankAccountService.list(customerId?.let { CustomerId(it) }).map { it.toResponse() }

 @PutMapping("/{accountId}")
 fun rename(
 @PathVariable accountId: UUID,
 @Valid @RequestBody request: UpdateAccountRequest
 ): AccountResponse =
 bankAccountService.rename(AccountId(accountId), request.displayName).toResponse()

 @PostMapping("/{accountId}/deposits")
 fun deposit(
 @PathVariable accountId: UUID,
 @Valid @RequestBody request: AmountRequest
 ): AccountResponse {
 // TODO(case-30): implementer faktisk innskudd via service (nå returneres bare gjeldende konto)
 return bankAccountService.get(AccountId(accountId)).toResponse()
 }

 @PostMapping("/{accountId}/withdrawals")
 fun withdraw(
 @PathVariable accountId: UUID,
 @Valid @RequestBody request: AmountRequest
 ): AccountResponse {
 val amount = request.amount ?: throw IllegalArgumentException("amount is required")
 return bankAccountService.withdraw(AccountId(accountId), Money.ofPositive(amount)).toResponse()
 }

 @DeleteMapping("/{accountId}")
 @ResponseStatus(HttpStatus.NO_CONTENT)
 fun delete(@PathVariable accountId: UUID) {
 bankAccountService.delete(AccountId(accountId))
 }
}
