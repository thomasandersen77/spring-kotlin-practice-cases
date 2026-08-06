package com.interview.case38.products

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Optional

class ProductServiceTest {
    @Test fun `service henter gjennom repository og mapper response`() {
        val repository = mock(ProductRepository::class.java)
        `when`(repository.findById(7)).thenReturn(Optional.of(ProductEntity(id=7, sku="K-7", name="Kotlin", stock=4)))
        assertThat(ProductService(repository).get(7)).isEqualTo(ProductResponse(7, "K-7", "Kotlin", 4, true))
    }
}
