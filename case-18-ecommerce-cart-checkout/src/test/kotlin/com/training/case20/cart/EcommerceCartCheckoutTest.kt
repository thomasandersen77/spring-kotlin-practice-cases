package com.training.case20.cart

import org.junit.jupiter.api.Test
import java.time.Instant

class EcommerceCartCheckoutTest {
 @Test
 fun `cart checkout should fail for empty cart`() {
 val cart = Cart()
 val useCase = CheckoutCartUseCase()

 useCase.checkout(cart, Instant.parse("2026-01-01T12:00:00Z"))
 }
}
