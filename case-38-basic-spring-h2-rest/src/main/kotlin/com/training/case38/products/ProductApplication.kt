package com.training.case38.products

import jakarta.persistence.*
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@SpringBootApplication class ProductApplication
fun main(args: Array<String>) { runApplication<ProductApplication>(*args) }

@Entity @Table(name = "products", uniqueConstraints = [UniqueConstraint(columnNames = ["sku"])])
class ProductEntity(
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
 @Column(nullable = false) var sku: String = "",
 @Column(nullable = false) var name: String = "",
 @Column(nullable = false) var stock: Int = 0,
 @Column(nullable = false) var active: Boolean = true
) {
 // TODO 1: Endre beholdningen og avvis negativ sluttverdi.
 fun changeStock(delta: Int) { TODO("Beskytt stock-invarianten") }
}

data class CreateProductRequest(@field:NotBlank val sku: String, @field:NotBlank val name: String, @field:Min(0) val initialStock: Int)
data class ChangeStockRequest(val delta: Int)
data class ProductResponse(val id: Long, val sku: String, val name: String, val stock: Int, val active: Boolean)

interface ProductRepository : JpaRepository<ProductEntity, Long> {
 fun findByActiveTrueOrderByNameAsc(): List<ProductEntity>
}

class ProductNotFound(id: Long) : RuntimeException("Produkt $id finnes ikke")
class StockConflict(message: String) : RuntimeException(message)

// TODO 2: Implementer mappingene uten å eksponere entity fra API-et.
fun CreateProductRequest.toEntity(): ProductEntity = TODO("Map request til entity")
fun ProductEntity.toResponse(): ProductResponse = TODO("Map entity til response")

@Service
class ProductService(private val repository: ProductRepository) {
 @Transactional fun create(request: CreateProductRequest): ProductResponse = TODO("Lagre produkt")
 @Transactional(readOnly = true) fun get(id: Long): ProductResponse = TODO("Hent eller kast ProductNotFound")
 @Transactional(readOnly = true) fun listActive(): List<ProductResponse> = TODO("Bruk repository-query og map")
 @Transactional fun changeStock(id: Long, delta: Int): ProductResponse = TODO("Hent, endre invariant og map")
}

@RestController @RequestMapping("/api/products")
class ProductController(private val service: ProductService) {
 @PostMapping fun create(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<ProductResponse> = ResponseEntity.status(HttpStatus.CREATED).body(service.create(request))
 @GetMapping("/{id}") fun get(@PathVariable id: Long) = service.get(id)
 @GetMapping fun list(): List<ProductResponse> = service.listActive()
 @PatchMapping("/{id}/stock") fun changeStock(@PathVariable id: Long, @RequestBody request: ChangeStockRequest) = service.changeStock(id, request.delta)
}

@RestControllerAdvice
class ProductExceptionHandler {
 // TODO 7: Begrunn og fullfør mappingen av domene-/applikasjonsfeil til HTTP.
 @ExceptionHandler(ProductNotFound::class) @ResponseStatus(HttpStatus.NOT_FOUND) fun notFound(ex: ProductNotFound) = mapOf("error" to ex.message)
 @ExceptionHandler(StockConflict::class) @ResponseStatus(HttpStatus.CONFLICT) fun conflict(ex: StockConflict) = mapOf("error" to ex.message)
}
