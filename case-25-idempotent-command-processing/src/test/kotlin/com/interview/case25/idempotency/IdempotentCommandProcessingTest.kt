package com.interview.case25.idempotency

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IdempotentCommandProcessingTest {

    @Test
    fun `idempotency key should be normalized`() {
        val key = IdempotencyKey.of("  Payment-Request-001  ")

        assertThat(key.value).isEqualTo("payment-request-001")
    }

    @Test
    fun `exercise duplicate key should return previous receipt without new gateway charge`() {
        // Lag testdouble for IdempotencyStore + PaymentGateway og verifiser:
        // 1) første kall går til gateway
        // 2) andre kall med samme key returnerer allerede prosessert receipt
        // 3) gateway blir ikke kalt på nytt
    }
}
