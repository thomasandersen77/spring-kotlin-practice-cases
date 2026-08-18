package com.training.case36.bank.adapter.web

import com.training.case36.bank.adapter.web.dto.AccountResponse
import com.training.case36.bank.adapter.web.dto.CreateAccountRequest
import com.training.case36.bank.adapter.web.dto.DepositRequest
import com.training.case36.bank.adapter.web.dto.TransferMoneyRequest
import com.training.case36.bank.adapter.web.dto.TransferResponse
import com.training.case36.bank.adapter.web.mapping.toCommand
import com.training.case36.bank.adapter.web.mapping.toResponse
import com.training.case36.bank.application.BankingService
import com.training.case36.bank.domain.AccountId
import jakarta.validation.Valid
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * Thin controller: receive the request, delegate to [BankingService], map the result. No business
 * rules, no `@Transactional`, no repository calls and no JPA knowledge belong here.
 *
 * TODO 13: wire each endpoint to the right use case in [BankingService] and map the result with
 * `BankingWebMapping`. See the README for the expected HTTP status per endpoint and error case.
 */
@RestController
@RequestMapping("/api")
class BankingController(private val bankingService: BankingService) {

	@PostMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	fun createAccount(@Valid @RequestBody request: CreateAccountRequest): AccountResponse =
		TODO(
			"TODO 13: kall bankingService.createAccount(request.toCommand()) og map til AccountResponse"
		)

	@GetMapping("/accounts/{accountId}")
	fun getAccount(@PathVariable accountId: UUID): AccountResponse =
		TODO(
			"TODO 13: kall bankingService.getAccount(AccountId(accountId)) og map til AccountResponse"
		)

	@PostMapping("/accounts/{accountId}/deposits")
	fun deposit(
		@PathVariable accountId: UUID,
		@Valid @RequestBody request: DepositRequest,
	): AccountResponse =
		TODO(
			"TODO 13: kall bankingService.deposit(request.toCommand(AccountId(accountId))) og map til AccountResponse"
		)

	@PostMapping("/transfers")
	fun transfer(@Valid @RequestBody request: TransferMoneyRequest): TransferResponse =
		TODO(
			"TODO 13: kall bankingService.transfer(request.toCommand()) og map til TransferResponse"
		)
}
