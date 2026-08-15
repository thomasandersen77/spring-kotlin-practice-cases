package com.training.case25.idempotency

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@JvmInline
value class PaymentId(val value: UUID)

@JvmInline
value class IdempotencyKey private constructor(val value: String) {
 companion object {
 fun of(raw: String): IdempotencyKey {
 val normalized = raw.trim().lowercase()
 require(normalized.isNotBlank()) { "idempotency key cannot be blank" }
 return IdempotencyKey(normalized)
 }
 }
}

data class ProcessPaymentCommand(
 val idempotencyKey: IdempotencyKey,
 val customerId: String,
 val amount: BigDecimal
)

data class PaymentReceipt(
 val paymentId: PaymentId,
 val processedAt: Instant,
 val customerId: String,
 val amount: BigDecimal
)

sealed class ProcessPaymentResult {
 data class Processed(val receipt: PaymentReceipt) : ProcessPaymentResult()
 data class AlreadyProcessed(val receipt: PaymentReceipt) : ProcessPaymentResult()
}

interface IdempotencyStore {
 fun find(key: IdempotencyKey): PaymentReceipt?
 fun save(key: IdempotencyKey, receipt: PaymentReceipt): Boolean
}

interface PaymentGateway {
 fun charge(customerId: String, amount: BigDecimal): PaymentReceipt
}

class ProcessPaymentUseCase(
 private val store: IdempotencyStore,
 private val paymentGateway: PaymentGateway
) {
 fun handle(command: ProcessPaymentCommand): ProcessPaymentResult {
 TODO("Implement idempotent command flow: lookup key, return existing receipt or charge once and store result")
 }
}
