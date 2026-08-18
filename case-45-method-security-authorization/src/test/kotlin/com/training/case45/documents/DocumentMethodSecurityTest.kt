package com.training.case45.documents

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.test.context.support.WithMockUser

@SpringBootTest
class DocumentMethodSecurityTest(@Autowired private val service: DocumentService) {

	@MockBean private lateinit var repository: DocumentRepository

	@Test
	@WithMockUser(username = "owner", roles = ["USER"])
	fun `tillatt bruker passerer method security`() {
		`when`(repository.findById(1)).thenReturn(Document(1, "owner", Classification.INTERNAL))

		assertThat(service.get(1).owner).isEqualTo("owner")
	}

	@Test
	@WithMockUser(username = "stranger", roles = ["USER"])
	fun `avvist bruker stoppes av method security`() {
		`when`(repository.findById(1)).thenReturn(Document(1, "owner", Classification.INTERNAL))

		assertThatThrownBy { service.get(1) }.isInstanceOf(AccessDeniedException::class.java)
	}
}
