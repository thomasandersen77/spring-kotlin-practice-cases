package com.training.case38.products

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ProductRepositoryTest(@Autowired val repository: ProductRepository) {
	@Test
	fun `aktive produkter lagres og hentes sortert pa navn`() {
		repository.saveAll(
			listOf(
				ProductEntity(sku = "B", name = "Zink", stock = 1),
				ProductEntity(sku = "A", name = "Alfa", stock = 2),
				ProductEntity(sku = "C", name = "Skjult", stock = 1, active = false),
			)
		)
		assertThat(repository.findByActiveTrueOrderByNameAsc().map { it.name })
			.containsExactly("Alfa", "Zink")
	}
}
