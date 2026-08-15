package com.training.case30.bank.persistence

import com.training.case30.bank.domain.AccountStatus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import java.math.BigDecimal
import java.util.UUID

@DataJpaTest
class CustomerAccountRepositoryJpaTest {

 @Autowired
 lateinit var customerRepository: SpringDataCustomerRepository

 @Autowired
 lateinit var accountRepository: SpringDataBankAccountRepository

 @Test
 fun `customer can be saved and fetched`() {
 val customer = customerRepository.save(CustomerEntity(UUID.randomUUID(), "Ola", "ola@example.no"))
 val fetched = customerRepository.findById(customer.id)
 assertThat(fetched).isPresent
 assertThat(fetched.get().email).isEqualTo("ola@example.no")
 }

 @Test
 fun `account can be saved and fetched with owner`() {
 val customer = customerRepository.save(CustomerEntity(UUID.randomUUID(), "Ola", "ola2@example.no"))
 val account = accountRepository.save(
 BankAccountEntity(
 id = UUID.randomUUID(),
 accountNumber = "15030011111",
 displayName = "Brukskonto",
 balance = BigDecimal("0.00"),
 status = AccountStatus.ACTIVE,
 customer = customer
 )
 )

 val fetched = accountRepository.findById(account.id)
 assertThat(fetched).isPresent
 assertThat(fetched.get().customer.id).isEqualTo(customer.id)
 }

 @Test
 fun `multiple accounts can belong to same customer`() {
 val customer = customerRepository.save(CustomerEntity(UUID.randomUUID(), "Kari", "kari2@example.no"))
 accountRepository.save(
 BankAccountEntity(UUID.randomUUID(), "15030022222", "A", BigDecimal("0.00"), AccountStatus.ACTIVE, customer)
 )
 accountRepository.save(
 BankAccountEntity(UUID.randomUUID(), "15030033333", "B", BigDecimal("0.00"), AccountStatus.ACTIVE, customer)
 )

 val result = accountRepository.findAllByCustomerId(customer.id)
 assertThat(result).hasSize(2)
 }

 @Test
 fun `email should be unique`() {
 customerRepository.save(CustomerEntity(UUID.randomUUID(), "A", "same@example.no"))
 assertThatThrownBy {
 customerRepository.saveAndFlush(CustomerEntity(UUID.randomUUID(), "B", "same@example.no"))
 }.isInstanceOf(DataIntegrityViolationException::class.java)
 }

 @Test
 fun `account number should be unique`() {
 val customer = customerRepository.save(CustomerEntity(UUID.randomUUID(), "Ola", "ola3@example.no"))
 accountRepository.saveAndFlush(
 BankAccountEntity(UUID.randomUUID(), "15030044444", "A", BigDecimal("0.00"), AccountStatus.ACTIVE, customer)
 )
 assertThatThrownBy {
 accountRepository.saveAndFlush(
 BankAccountEntity(UUID.randomUUID(), "15030044444", "B", BigDecimal("0.00"), AccountStatus.ACTIVE, customer)
 )
 }.isInstanceOf(DataIntegrityViolationException::class.java)
 }

 @Test
 fun `updated balance should persist`() {
 val customer = customerRepository.save(CustomerEntity(UUID.randomUUID(), "Ola", "ola4@example.no"))
 val account = accountRepository.saveAndFlush(
 BankAccountEntity(UUID.randomUUID(), "15030055555", "A", BigDecimal("0.00"), AccountStatus.ACTIVE, customer)
 )
 account.balance = BigDecimal("123.45")
 accountRepository.saveAndFlush(account)

 val fetched = accountRepository.findById(account.id).orElseThrow()
 assertThat(fetched.balance).isEqualByComparingTo("123.45")
 }

 @Test
 fun `status enum should be stored as string`() {
 val customer = customerRepository.save(CustomerEntity(UUID.randomUUID(), "Ola", "ola5@example.no"))
 val account = accountRepository.saveAndFlush(
 BankAccountEntity(UUID.randomUUID(), "15030066666", "A", BigDecimal("0.00"), AccountStatus.CLOSED, customer)
 )
 val fetched = accountRepository.findById(account.id).orElseThrow()
 assertThat(fetched.status).isEqualTo(AccountStatus.CLOSED)
 }
}
