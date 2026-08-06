package com.interview.case25.idempotency

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class IdempotentCommandProcessingTest {

    @Test
    fun `idempotency key should be normalized`() {
        val key = IdempotencyKey.of("  Payment-Request-001  ")

        assertThat(key.value).isEqualTo("payment-request-001")
    }

    @Test
    fun `exercise duplicate key should return previous receipt without new gateway charge`() {
        val store = FakeStore()
        var charges = 0
        val receipt = receipt()
        val useCase = ProcessPaymentUseCase(store, object : PaymentGateway {
            override fun charge(customerId: String, amount: BigDecimal): PaymentReceipt {
                charges++
                return receipt
            }
        })
        val command = ProcessPaymentCommand(IdempotencyKey.of("KEY"), "customer", BigDecimal.TEN)

        assertThat(useCase.handle(command)).isEqualTo(ProcessPaymentResult.Processed(receipt))
        assertThat(useCase.handle(command)).isEqualTo(ProcessPaymentResult.AlreadyProcessed(receipt))
        assertThat(charges).isEqualTo(1)
    }

    @Test
    fun `invalid business input should fail before gateway`() {
        var charges = 0
        val useCase = ProcessPaymentUseCase(FakeStore(), object : PaymentGateway {
            override fun charge(customerId: String, amount: BigDecimal): PaymentReceipt {
                charges++
                return receipt()
            }
        })
        assertThatThrownBy {
            useCase.handle(ProcessPaymentCommand(IdempotencyKey.of("key"), "", BigDecimal.ONE))
        }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy {
            useCase.handle(ProcessPaymentCommand(IdempotencyKey.of("key"), "c", BigDecimal.ZERO))
        }.isInstanceOf(IllegalArgumentException::class.java)
        assertThat(charges).isZero()
    }

    private fun receipt() = PaymentReceipt(
        PaymentId(UUID.randomUUID()), Instant.parse("2026-01-01T00:00:00Z"), "customer", BigDecimal.TEN
    )

    private class FakeStore : IdempotencyStore {
        private val receipts = mutableMapOf<IdempotencyKey, PaymentReceipt>()
        override fun find(key: IdempotencyKey) = receipts[key]
        override fun save(key: IdempotencyKey, receipt: PaymentReceipt): Boolean = receipts.putIfAbsent(key, receipt) == null
    }
}
