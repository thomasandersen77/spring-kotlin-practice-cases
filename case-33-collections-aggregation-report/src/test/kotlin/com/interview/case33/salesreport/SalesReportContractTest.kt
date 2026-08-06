package com.interview.case33.salesreport

import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class SalesReportContractTest {

    @Test
    fun `negative top seller limit is rejected`() {
        assertThatIllegalArgumentException()
            .isThrownBy { emptyList<Order>().topSellingSkus(-1) }
            .withMessage("limit cannot be negative")
    }
}
