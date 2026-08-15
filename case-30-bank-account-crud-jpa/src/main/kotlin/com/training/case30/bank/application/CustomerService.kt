package com.training.case30.bank.application

import com.training.case30.bank.domain.Customer
import com.training.case30.bank.domain.CustomerHasAccountsException
import com.training.case30.bank.domain.CustomerId
import com.training.case30.bank.domain.CustomerNotFoundException
import com.training.case30.bank.domain.EmailAlreadyInUseException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CustomerService(
 private val customerRepository: CustomerRepository,
 private val bankAccountRepository: BankAccountRepository
) {
 @Transactional
 fun create(name: String, email: String): Customer {
 if (customerRepository.existsByEmail(email)) {
 throw EmailAlreadyInUseException(email)
 }

 val customer = Customer(
 id = CustomerId(UUID.randomUUID()),
 name = name,
 email = email
 )
 return customerRepository.save(customer)
 }

 @Transactional(readOnly = true)
 fun get(customerId: CustomerId): Customer =
 customerRepository.findById(customerId) ?: throw CustomerNotFoundException(customerId)

 @Transactional(readOnly = true)
 fun list(): List<Customer> = customerRepository.findAll()

 @Transactional
 fun update(customerId: CustomerId, name: String, email: String): Customer {
 val existing = customerRepository.findById(customerId) ?: throw CustomerNotFoundException(customerId)

 // TODO(case-30): håndter konflikt ved e-postendring hvis e-post allerede er i bruk av annen kunde
 val updated = existing.rename(name).changeEmail(email)
 return customerRepository.save(updated)
 }

 @Transactional
 fun delete(customerId: CustomerId) {
 val existing = customerRepository.findById(customerId) ?: throw CustomerNotFoundException(customerId)
 if (bankAccountRepository.existsByCustomerId(existing.id)) {
 throw CustomerHasAccountsException(customerId)
 }
 customerRepository.deleteById(customerId)
 }
}
