package com.training.case45.documents

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

class DocumentAccessPolicyTest {

 private val repository = mock(DocumentRepository::class.java)
 private val policy = DocumentAccessPolicy(repository)

 @Test
 fun `eier kan lese internt dokument`() {
 `when`(repository.findById(1)).thenReturn(Document(1, "user-1", Classification.INTERNAL))

 assertThat(policy.canRead(authentication("user-1", "ROLE_USER"), 1)).isTrue()
 }

 @Test
 fun `annen bruker avvises mens reviewer kan lese`() {
 `when`(repository.findById(2)).thenReturn(Document(2, "user-1", Classification.INTERNAL))

 assertThat(policy.canRead(authentication("user-2", "ROLE_USER"), 2)).isFalse()
 assertThat(policy.canRead(authentication("reviewer", "ROLE_REVIEWER"), 2)).isTrue()
 }

 private fun authentication(subject: String, authority: String) =
 UsernamePasswordAuthenticationToken(subject, "n/a", listOf(SimpleGrantedAuthority(authority)))
}
