package com.training.case30.bank.persistence

import com.training.case30.bank.domain.AccountStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "bank_accounts")
class BankAccountEntity(
 @Id
 @Column(nullable = false, updatable = false)
 var id: UUID,

 @Column(nullable = false, unique = true, updatable = false)
 var accountNumber: String,

 @Column(nullable = false)
 var displayName: String,

 @Column(nullable = false, precision = 19, scale = 2)
 var balance: BigDecimal,

 @Enumerated(EnumType.STRING)
 @Column(nullable = false)
 var status: AccountStatus,

 @ManyToOne(fetch = FetchType.LAZY, optional = false)
 @JoinColumn(name = "customer_id", nullable = false)
 var customer: CustomerEntity
)
