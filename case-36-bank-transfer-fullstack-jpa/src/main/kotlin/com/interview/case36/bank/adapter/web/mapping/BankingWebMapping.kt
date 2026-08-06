package com.interview.case36.bank.adapter.web.mapping

import com.interview.case36.bank.adapter.web.dto.AccountResponse
import com.interview.case36.bank.adapter.web.dto.CreateAccountRequest
import com.interview.case36.bank.adapter.web.dto.DepositRequest
import com.interview.case36.bank.adapter.web.dto.TransferMoneyRequest
import com.interview.case36.bank.adapter.web.dto.TransferResponse
import com.interview.case36.bank.application.CreateAccountCommand
import com.interview.case36.bank.application.DepositCommand
import com.interview.case36.bank.application.TransferMoneyCommand
import com.interview.case36.bank.domain.AccountId
import com.interview.case36.bank.domain.BankAccount
import com.interview.case36.bank.domain.BankTransfer
import com.interview.case36.bank.domain.Money

/**
 * Explicit mapping at the web boundary - the same kind of training as case 31, now wired into a real
 * Spring MVC controller. Amounts are converted with `Money.ofKroner`/`Money.toKroner`, never by
 * multiplying/dividing by 100 directly in this file.
 */

/**
 * TODO 7: Map an incoming [CreateAccountRequest] to a [CreateAccountCommand].
 */
fun CreateAccountRequest.toCommand(): CreateAccountCommand =
    CreateAccountCommand(ownerName)

/**
 * TODO 7: Map an incoming [DepositRequest] (plus the path-variable account id) to a [DepositCommand].
 */
fun DepositRequest.toCommand(accountId: AccountId): DepositCommand =
    DepositCommand(accountId, Money.ofKroner(requireNotNull(amount) { "amount is required" }))

/**
 * TODO 7: Map an incoming [TransferMoneyRequest] to a [TransferMoneyCommand].
 */
fun TransferMoneyRequest.toCommand(): TransferMoneyCommand =
    TransferMoneyCommand(
        AccountId(requireNotNull(fromAccountId) { "fromAccountId is required" }),
        AccountId(requireNotNull(toAccountId) { "toAccountId is required" }),
        Money.ofKroner(requireNotNull(amount) { "amount is required" })
    )

/**
 * TODO 8: Map a [BankAccount] to its API representation.
 */
fun BankAccount.toResponse(): AccountResponse =
    AccountResponse(id.value, ownerName, status.name, balance.toKroner())

/**
 * TODO 8: Map a [BankTransfer] to its API representation.
 */
fun BankTransfer.toResponse(): TransferResponse =
    TransferResponse(id.value, fromAccountId.value, toAccountId.value, amount.toKroner(), executedAt)
