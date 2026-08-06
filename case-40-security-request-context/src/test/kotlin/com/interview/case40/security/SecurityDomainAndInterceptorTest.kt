package com.interview.case40.security
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

class SecurityDomainAndInterceptorTest {
    @Test fun `bruker kan lese egen sak men ikke andres og manager kan lese alle`() {
        val own = CaseEntity(id=1, ownerSubject="u1")
        assertThat(CurrentUser("u1", setOf(Role.USER)).canRead(own)).isTrue()
        assertThat(CurrentUser("u2", setOf(Role.USER)).canRead(own)).isFalse()
        assertThat(CurrentUser("m1", setOf(Role.MANAGER)).canRead(own)).isTrue()
    }
    @Test fun `innsendt correlation id gjenbrukes og ryddes`() {
        val interceptor=CorrelationInterceptor(); val request=MockHttpServletRequest().apply { addHeader("X-Correlation-ID", "req-123") }; val response=MockHttpServletResponse()
        interceptor.preHandle(request,response,Any()); assertThat(CorrelationContext.get()).isEqualTo("req-123"); assertThat(response.getHeader("X-Correlation-ID")).isEqualTo("req-123")
        interceptor.afterCompletion(request,response,Any(),null); assertThatThrownBy { CorrelationContext.get() }.isInstanceOf(IllegalStateException::class.java)
    }
    @Test fun `context ryddes ogsa etter exception og lekker ikke til neste request`() {
        val interceptor=CorrelationInterceptor(); val first=MockHttpServletRequest(); interceptor.preHandle(first,MockHttpServletResponse(),Any()); interceptor.afterCompletion(first,MockHttpServletResponse(),Any(),RuntimeException("boom"))
        val secondResponse=MockHttpServletResponse(); interceptor.preHandle(MockHttpServletRequest(),secondResponse,Any()); assertThat(secondResponse.getHeader("X-Correlation-ID")).isNotBlank()
    }
}
