package com.interview.case30.bank.domain

data class Customer(
    val id: CustomerId,
    val name: String,
    val email: String
) {
    init {
        require(name.isNotBlank()) { "Customer name cannot be blank" }
        require(email.isNotBlank()) { "Customer email cannot be blank" }
        require("@" in email) { "Customer email must contain @" }
    }

    fun rename(newName: String): Customer {
        require(newName.isNotBlank()) { "Customer name cannot be blank" }
        return copy(name = newName.trim())
    }

    fun changeEmail(newEmail: String): Customer {
        require(newEmail.isNotBlank()) { "Customer email cannot be blank" }
        require("@" in newEmail) { "Customer email must contain @" }
        return copy(email = newEmail.trim())
    }
}
