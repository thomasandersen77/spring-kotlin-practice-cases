import java.math.BigDecimal

/**
 * REN KOTLIN / DOMENEMODELLERING
 *
 * TODO:
 *  - Bruk value objects
 *  - Unngå primitive obsession
 *  - Bruk sealed class for rabatter
 *  - Skriv tester først
 */

@JvmInline
value class CustomerId(val value: String)

@JvmInline
value class ProductId(val value: String)

data class Money(val amount: BigDecimal) {
    operator fun plus(other: Money): Money = Money(amount + other.amount)
    operator fun minus(other: Money): Money = Money(amount - other.amount)
    operator fun times(quantity: Int): Money = Money(amount.multiply(BigDecimal(quantity)))
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
    val quantity: Int,
    val unitPrice: Money
) {
    fun lineTotal(): Money = unitPrice * quantity
}

sealed class Discount {
    data class Percentage(val percent: Int) : Discount()
    data class FixedAmount(val amount: Money) : Discount()
    data object NoDiscount : Discount()
}

class PricingService {
    fun calculateTotal(basket: Basket, discount: Discount): Money {
        // TODO("Implementer rabattregler")
        if (discount is Discount.NoDiscount) {
            return basket.subtotal()
        }

        if (discount is Discount.Percentage) {
            val factor = BigDecimal.ONE - BigDecimal(discount.percent).divide(BigDecimal(100))
            val subtotal = basket.subtotal()
            return subtotal * factor
        }
        if (discount is Discount.FixedAmount) {
            val reduced = basket.subtotal() - discount.amount
            return if (reduced.amount < BigDecimal.ZERO) Money.ZERO else reduced
        }
        return basket.subtotal()
    }
}
