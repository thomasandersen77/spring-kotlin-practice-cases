package com.interview.case20.cart

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Instant

class EcommerceCartCheckoutTest {
    @Test
    fun `cart checkout should fail for empty cart`() {
        val cart = Cart()
        val useCase = CheckoutCartUseCase()

        assertThatThrownBy { useCase.checkout(cart, Instant.parse("2026-01-01T12:00:00Z")) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("empty")
    }

    @Test
    fun `adding same product should merge positive quantities`() {
        val cart = Cart()

        cart.addLine(ProductId("P-1"), 2)
        cart.addLine(ProductId("P-1"), 3)

        assertThat(cart.lines).containsExactly(CartLine(ProductId("P-1"), 5))
        assertThatThrownBy { cart.addLine(ProductId("P-2"), 0) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `checkout should record state and timestamp and prevent later changes`() {
        val cart = Cart()
        val at = Instant.parse("2026-01-01T12:00:00Z")
        cart.addLine(ProductId("P-1"), 1)

        CheckoutCartUseCase().checkout(cart, at)

        assertThat(cart.status).isEqualTo(CartStatus.CHECKED_OUT)
        assertThat(cart.checkedOutAt).isEqualTo(at)
        assertThatThrownBy { cart.checkout(at.plusSeconds(1)) }
            .isInstanceOf(IllegalStateException::class.java)
        assertThatThrownBy { cart.addLine(ProductId("P-2"), 1) }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
