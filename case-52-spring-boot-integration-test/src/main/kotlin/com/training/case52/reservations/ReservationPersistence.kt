package com.training.case52.reservations

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "products")
class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, unique = true)
    var productCode: String = "",
    @Column(nullable = false)
    var stock: Int = 0
)

@Entity
@Table(name = "reservations")
class ReservationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var productCode: String = "",
    @Column(nullable = false)
    var quantity: Int = 0,
    @Column(nullable = false)
    var customerEmail: String = ""
)

interface ProductRepository : JpaRepository<ProductEntity, Long> {
    fun findByProductCode(productCode: String): ProductEntity?
}

interface ReservationRepository : JpaRepository<ReservationEntity, Long>

