package com.interview.case30.bank.persistence

import com.interview.case30.bank.domain.AccountId
import com.interview.case30.bank.domain.AccountNumber
import com.interview.case30.bank.domain.BankAccount
import com.interview.case30.bank.domain.Customer
import com.interview.case30.bank.domain.CustomerId
import com.interview.case30.bank.domain.Money

fun CustomerEntity.toDomain(): Customer = Customer(
    id = CustomerId(id),
    name = name,
    email = email
)

fun Customer.toEntity(): CustomerEntity = CustomerEntity(
    id = id.value,
    name = name,
    email = email
)

fun BankAccountEntity.toDomain(): BankAccount = BankAccount(
    id = AccountId(id),
    accountNumber = AccountNumber(accountNumber),
    customerId = CustomerId(customer.id),
    displayName = displayName,
    balance = Money.of(balance),
    status = status
)

fun BankAccount.toEntity(customer: CustomerEntity): BankAccountEntity = BankAccountEntity(
    id = id.value,
    accountNumber = accountNumber.value,
    displayName = displayName,
    balance = balance.amount,
    status = status,
    customer = customer
)
