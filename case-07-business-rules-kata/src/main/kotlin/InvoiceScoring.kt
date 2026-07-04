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
    /**
     * Øvingspunkter:
     * - Legg til flere rabattkoder og dokumenter prioritet.
     * - Avklar avrunding hvis rabatten blir desimal.
     * - Beskriv hvilke regler som hører hjemme i domain vs application service.
     */
    fun score(request: InvoiceRequest): InvoiceResponse {
        val subtotal = request.lines.sumOf { it.unitPrice * it.quantity }

        val vipDiscount = if (request.vipCustomer) subtotal / 10 else 0
        val codeDiscount = when (request.discountCode?.trim()?.uppercase()) {
            "SAVE50" -> 50
            else -> 0
        }

        val discount = vipDiscount + codeDiscount
        val total = (subtotal - discount).coerceAtLeast(0)

        return InvoiceResponse(
            subtotal = subtotal,
            discount = discount,
            total = total
        )
    }
}
