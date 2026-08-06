package com.interview.case20.cart

import java.time.Instant

@JvmInline
value class ProductId(val value: String) {
    init { require(value.isNotBlank()) { "product id cannot be blank" } }
}

data class CartLine(val productId: ProductId, val quantity: Int)

enum class CartStatus { OPEN, CHECKED_OUT }

class Cart {
    private val linesByProduct = linkedMapOf<ProductId, CartLine>()
    val lines: List<CartLine> get() = linesByProduct.values.toList()
    var status: CartStatus = CartStatus.OPEN
        private set
    var checkedOutAt: Instant? = null
        private set

    fun addLine(productId: ProductId, quantity: Int) {
        ensureOpen()
        require(quantity > 0) { "quantity must be positive" }
        val current = linesByProduct[productId]
        val mergedQuantity = Math.addExact(current?.quantity ?: 0, quantity)
        linesByProduct[productId] = CartLine(productId, mergedQuantity)
    }

    fun checkout(at: Instant) {
        ensureOpen()
        check(linesByProduct.isNotEmpty()) { "empty cart cannot be checked out" }
        status = CartStatus.CHECKED_OUT
        checkedOutAt = at
    }

    private fun ensureOpen() = check(status == CartStatus.OPEN) { "cart is already checked out" }
}

class CheckoutCartUseCase {
    fun checkout(cart: Cart, at: Instant) {
        cart.checkout(at)
    }
}
