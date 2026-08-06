import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class InvoiceLine(
    val unitPrice: Int,
    val quantity: Int
)

data class InvoiceRequest(
    val lines: List<InvoiceLine>,
    val vipCustomer: Boolean,
    val discountCode: String?
)

data class InvoiceResponse(
    val subtotal: Int,
    val discount: Int,
    val total: Int
)

@RestController
@RequestMapping("/invoice")
class InvoiceController(
    private val scoring: InvoiceScoring = InvoiceScoring()
) {
    @PostMapping("/score")
    fun score(@RequestBody request: InvoiceRequest): InvoiceResponse {
        return scoring.score(request)
    }
}

class InvoiceScoring {
    private val codeDiscounts: Map<String, (Int) -> Int> = mapOf(
        "SAVE50" to { 50 },
        "SAVE10" to { subtotal -> subtotal / 10 },
        "HALF" to { subtotal -> subtotal / 2 }
    )

    fun score(request: InvoiceRequest): InvoiceResponse {
        require(request.lines.isNotEmpty()) { "invoice must contain at least one line" }
        request.lines.forEach {
            require(it.unitPrice >= 0) { "unit price cannot be negative" }
            require(it.quantity > 0) { "quantity must be positive" }
        }

        val subtotal = request.lines.sumOf { Math.multiplyExact(it.unitPrice, it.quantity) }
        val normalizedCode = request.discountCode?.trim()?.uppercase()?.takeUnless(String::isEmpty)
        val codeRule = normalizedCode?.let {
            requireNotNull(codeDiscounts[it]) { "unknown discount code: $it" }
        }

        val vipDiscount = if (request.vipCustomer) subtotal / 10 else 0
        val codeDiscount = codeRule?.invoke(subtotal) ?: 0

        val discount = Math.addExact(vipDiscount, codeDiscount).coerceAtMost(subtotal)
        val total = subtotal - discount

        return InvoiceResponse(
            subtotal = subtotal,
            discount = discount,
            total = total
        )
    }
}
