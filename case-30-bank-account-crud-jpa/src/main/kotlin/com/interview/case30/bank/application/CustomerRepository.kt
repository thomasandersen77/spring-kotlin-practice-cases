package com.interview.case30.bank.application

import com.interview.case30.bank.domain.Customer
import com.interview.case30.bank.domain.CustomerId

interface CustomerRepository {
    fun save(customer: Customer): Customer
    fun findById(id: CustomerId): Customer?
    fun findAll(): List<Customer>
    fun existsByEmail(email: String): Boolean
    fun existsByEmailExcludingId(email: String, excludedId: CustomerId): Boolean
    fun deleteById(id: CustomerId)
}
