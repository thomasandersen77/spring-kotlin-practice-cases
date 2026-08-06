package com.interview.case30.bank.domain

enum class AccountStatus {
    ACTIVE,
    CLOSED
}

class BankAccount(
    val id: AccountId,
    val accountNumber: AccountNumber,
    val customerId: CustomerId,
    displayName: String,
    balance: Money = Money.ZERO,
    status: AccountStatus = AccountStatus.ACTIVE
) {
    var displayName: String = displayName
        private set

    var balance: Money = balance
        private set

    var status: AccountStatus = status
        private set

    init {
        require(displayName.isNotBlank()) { "Display name cannot be blank" }
    }

    fun rename(newDisplayName: String) {
        require(newDisplayName.isNotBlank()) { "Display name cannot be blank" }
        displayName = newDisplayName.trim()
    }

    fun deposit(amount: Money) {
        require(amount > Money.ZERO) { "Deposit amount must be greater than zero" }
        if (status == AccountStatus.CLOSED) throw AccountClosedException(id)
        balance = balance + amount
    }

    fun withdraw(amount: Money) {
        require(amount > Money.ZERO) { "Withdrawal amount must be greater than zero" }
        if (status == AccountStatus.CLOSED) {
            throw AccountClosedException(id)
        }
        if (balance < amount) throw InsufficientFundsException(id)
        balance = balance.subtractSafely(amount)
    }

    fun close() {
        if (balance != Money.ZERO) throw AccountHasBalanceException(id)
        status = AccountStatus.CLOSED
    }

    fun canBeDeleted(): Boolean = balance == Money.ZERO
}
