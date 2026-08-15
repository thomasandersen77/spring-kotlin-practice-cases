package com.training.case30.bank.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "customers")
class CustomerEntity(
 @Id
 @Column(nullable = false, updatable = false)
 var id: UUID,

 @Column(nullable = false)
 var name: String,

 @Column(nullable = false, unique = true)
 var email: String
)
