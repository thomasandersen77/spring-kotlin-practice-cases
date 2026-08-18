package com.training.case20.cart

import java.time.Instant

@JvmInline value class ProductId(val value: String)

data class CartLine(val productId: ProductId, val quantity: Int)

class Cart {
	fun addLine(productId: ProductId, quantity: Int) {
		TODO(
			"Implement aggregate invariant rules: positive quantity, duplicate product merge strategy, and immutable/readable state transitions"
		)
	}

	fun checkout(at: Instant) {
		TODO(
			"Implement checkout transition with protection against empty cart and repeated checkout"
		)
	}
}

class CheckoutCartUseCase {
	fun checkout(cart: Cart, at: Instant) {
		TODO("Implement application boundary orchestration and make transaction intent explicit")
	}
}
