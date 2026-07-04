package com.interview.case20.cart

import java.time.Instant

@JvmInline
value class ProductId(val value: String)

data class CartLine(val productId: ProductId, val quantity: Int)

class Cart {
    fun addLine(productId: ProductId, quantity: Int) {
        TODO("Implement merge quantity and positive quantity rules")
    }

    fun checkout(at: Instant) {
        TODO("Implement checkout transition")
    }
}

class CheckoutCartUseCase {
    fun checkout(cart: Cart, at: Instant) {
        TODO("Implement transaction/application boundary")
    }
}
