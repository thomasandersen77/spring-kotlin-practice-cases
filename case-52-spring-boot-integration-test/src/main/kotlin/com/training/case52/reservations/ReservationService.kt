package com.training.case52.reservations

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

data class ReservationResult(
	val id: Long,
	val productCode: String,
	val quantity: Int,
	val customerEmail: String,
	val remainingStock: Int,
)

class ProductNotFound(productCode: String) : RuntimeException("Produkt $productCode finnes ikke")

class ReservationNotFound(id: Long) : RuntimeException("Reservasjon $id finnes ikke")

class InsufficientStock(productCode: String) :
	RuntimeException("Ikke nok beholdning for $productCode")

@Service
class ReservationService(
	private val products: ProductRepository,
	private val reservations: ReservationRepository,
) {
	@Transactional
	fun reserve(productCode: String, quantity: Int, customerEmail: String): ReservationResult {
		val product = products.findByProductCode(productCode) ?: throw ProductNotFound(productCode)
		if (quantity > product.stock) throw InsufficientStock(productCode)

		product.stock -= quantity
		val reservation =
			reservations.save(
				ReservationEntity(
					productCode = productCode,
					quantity = quantity,
					customerEmail = customerEmail,
				)
			)

		return reservation.toResult(product.stock)
	}

	@Transactional(readOnly = true)
	fun find(id: Long): ReservationResult {
		val reservation = reservations.findById(id).orElseThrow { ReservationNotFound(id) }
		val remainingStock =
			products.findByProductCode(reservation.productCode)?.stock
				?: throw ProductNotFound(reservation.productCode)
		return reservation.toResult(remainingStock)
	}

	private fun ReservationEntity.toResult(remainingStock: Int) =
		ReservationResult(
			id = requireNotNull(id),
			productCode = productCode,
			quantity = quantity,
			customerEmail = customerEmail,
			remainingStock = remainingStock,
		)
}
