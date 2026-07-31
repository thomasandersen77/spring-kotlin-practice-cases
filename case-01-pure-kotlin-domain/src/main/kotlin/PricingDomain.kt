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
        require(value > 0, { "Quantity must be greater than zero" })
    }
}

data class Money(val amount: BigDecimal) {

    init {
        require(amount >= BigDecimal.ZERO,{ "Amount cant be less than zero" })
    }

    operator fun plus(other: Money): Money = Money(amount + other.amount)
    operator fun minus(other: Money): Money = Money(amount - other.amount)
    operator fun times(quantity: Quantity): Money = Money(amount.multiply(BigDecimal(quantity.value.toLong())))
    operator fun times(factor: BigDecimal): Money = Money(amount.multiply(factor))

    companion object {
        val ZERO = Money(BigDecimal.ZERO)
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
            require(percent in 0..100, { "Discount percent must be between 0 and 100" } )
        }
    }
    data class FixedAmount(val amount: Money) : Discount()
    data object NoDiscount : Discount()
}

class PricingService {
    fun calculateTotal(basket: Basket, discount: Discount): Money {
        // Neste steg: utvid regler og validering uten å ofre lesbarhet.
        val subTotalLines = basket.subtotal()
        when(discount) {
                Discount.NoDiscount -> return basket.subtotal()
            is Discount.FixedAmount -> return Money(
                (subTotalLines.amount - discount.amount.amount).coerceAtLeast(BigDecimal.ZERO)
            )
            is Discount.Percentage -> {
                val factor = BigDecimal.ONE - BigDecimal(discount.percent).divide(BigDecimal(100)).setScale(2, RoundingMode.HALF_UP)

                val subtotal = basket.subtotal()
                return subtotal * factor
            }
        }
    }
}
