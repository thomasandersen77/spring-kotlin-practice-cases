package com.training.case38.products

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductDomainTest {
	@Test
	fun `beholdning kan ikke bli negativ`() {
		val product = ProductEntity(stock = 2)
		assertThatThrownBy { product.changeStock(-3) }.isInstanceOf(StockConflict::class.java)
		assertThat(product.stock).isEqualTo(2)
	}
}
