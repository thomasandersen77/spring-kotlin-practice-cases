import java.math.BigDecimal
import java.math.RoundingMode

/**
 * REN KOTLIN / DOMENEMODELLERING
 *
 * CASE-FOKUS:
 *  - Bruk value objects for å redusere primitive obsession.
 *  - Hold rabattregler eksplisitte og lesbare.
 *  - Valider input nær konstruktørene der det gir mening.
 *  - La testene beskrive kontrakten før videre refaktorering.
 */

@JvmInline
value class CustomerId(val value: String)

@JvmInline
value class ProductId(val value: String)

@JvmInline
value class Quantity(val value: Int) {
    init {
        require(value > 0) { "Quantity must be greater than zero" }
    }
}

data class Money(val amount: BigDecimal) {

    init {
        require(amount >= BigDecimal.ZERO) { "Money cannot have a negative amount" }
    }

    operator fun plus(other: Money): Money = Money(amount + other.amount)
    operator fun minus(other: Money): Money = Money(amount - other.amount)
    operator fun times(quantity: Quantity): Money = Money(amount * quantity.value.toBigDecimal())
    operator fun times(factor: BigDecimal): Money = Money(amount.multiply(factor))

    companion object {
        val ZERO = Money(BigDecimal.ZERO)

        const val DEFAULT_SCALE = 2
        val DEFAULT_ROUNDING_MODE: RoundingMode = RoundingMode.HALF_UP
    }
}

data class Basket(
    val customerId: CustomerId,
    val lines: List<BasketLine>
) {

    fun subtotal(): Money =
        lines.fold(Money.ZERO) { acc, line -> acc + line.lineTotal() }
}

data class BasketLine(
    val productId: ProductId,
    val quantity: Quantity,
    val unitPrice: Money
) {
    fun lineTotal(): Money = unitPrice * quantity
}

sealed class Discount {
    data class Percentage(val percent: Int) : Discount() {
        init {
            require(percent in 0..100) { "Discount percent must be between 0 and 100" }
        }
    }

    data class FixedAmount(val amount: Money) : Discount()
    data object NoDiscount : Discount()
}

class PricingService {
    fun calculateTotal(basket: Basket, discount: Discount): Money {
        val subtotal = basket.subtotal()

        return when (discount) {
            Discount.NoDiscount -> subtotal
            is Discount.FixedAmount -> Money(
                (subtotal.amount - discount.amount.amount).coerceAtLeast(BigDecimal.ZERO)
            )
            is Discount.Percentage -> percentageDiscount(subtotal, discount.percent)
        }
    }

    private fun percentageDiscount(subtotal: Money, percent: Int): Money {
        val remainingPercent = (100 - percent).toBigDecimal()
        val discountedAmount = subtotal.amount
            .multiply(remainingPercent)
            .divide(BigDecimal.valueOf(100))
            .setScale(Money.DEFAULT_SCALE, Money.DEFAULT_ROUNDING_MODE)

        return Money(discountedAmount)
    }
}
