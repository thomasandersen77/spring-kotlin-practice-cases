package com.interview.case49.quotes

import org.assertj.core.api.Assertions.assertThat
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test

class QuoteHttpTest {

    private val service = QuoteService { request ->
        QuoteResponse(request.productCode, request.quantity, request.quantity * 1_500L)
    }

    @Test
    fun `gyldig request returnerer beregnet tilbud`() {
        val response = quoteApp(service)(
            Request(POST, "/quotes")
                .header("Content-Type", "application/json")
                .header("X-Request-ID", "req-1")
                .body("""{"productCode":"KOTLIN","quantity":2}""")
        )

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("X-Request-ID")).isEqualTo("req-1")
        assertThat(response.bodyString()).contains("\"totalOre\":3000")
    }

    @Test
    fun `ugyldig quantity gir 400 uten servicekall`() {
        val response = quoteApp(service)(
            Request(POST, "/quotes")
                .header("Content-Type", "application/json")
                .body("""{"productCode":"KOTLIN","quantity":0}""")
        )

        assertThat(response.status).isEqualTo(Status.BAD_REQUEST)
    }
}
