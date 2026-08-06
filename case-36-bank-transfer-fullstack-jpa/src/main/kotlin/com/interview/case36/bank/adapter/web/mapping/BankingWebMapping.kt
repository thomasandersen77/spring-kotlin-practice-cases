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

/**
 * Explicit mapping at the web boundary - the same kind of training as case 31, now wired into a real
 * Spring MVC controller. Amounts are converted with `Money.ofKroner`/`Money.toKroner`, never by
 * multiplying/dividing by 100 directly in this file.
 */

/**
 * TODO 7: Map an incoming [CreateAccountRequest] to a [CreateAccountCommand].
 */
fun CreateAccountRequest.toCommand(): CreateAccountCommand =
    TODO("TODO 7: map CreateAccountRequest til CreateAccountCommand")

/**
 * TODO 7: Map an incoming [DepositRequest] (plus the path-variable account id) to a [DepositCommand].
 */
fun DepositRequest.toCommand(accountId: AccountId): DepositCommand =
    TODO("TODO 7: map DepositRequest til DepositCommand")

/**
 * TODO 7: Map an incoming [TransferMoneyRequest] to a [TransferMoneyCommand].
 */
fun TransferMoneyRequest.toCommand(): TransferMoneyCommand =
    TODO("TODO 7: map TransferMoneyRequest til TransferMoneyCommand")

/**
 * TODO 8: Map a [BankAccount] to its API representation.
 */
fun BankAccount.toResponse(): AccountResponse =
    TODO("TODO 8: map BankAccount til AccountResponse")

/**
 * TODO 8: Map a [BankTransfer] to its API representation.
 */
fun BankTransfer.toResponse(): TransferResponse =
    TODO("TODO 8: map BankTransfer til TransferResponse")
