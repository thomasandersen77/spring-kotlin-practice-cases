package com.interview.case13.picklist

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class WarehousePickListTest {
    @Test
    fun `should allow adding a valid pick line`() {
        val pickList = PickList()

        pickList.addLine(Sku("SKU-1"), Quantity(2))

        assertThat(pickList.lines).containsExactly(PickLine(Sku("SKU-1"), Quantity(2), false))
    }

    @Test
    fun `invalid quantity and duplicate sku should be rejected`() {
        assertThatThrownBy { Quantity(0) }.isInstanceOf(IllegalArgumentException::class.java)
        val pickList = PickList()
        pickList.addLine(Sku("SKU-1"), Quantity(1))
        assertThatThrownBy { pickList.addLine(Sku("SKU-1"), Quantity(2)) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `unknown sku and repeated picking should be rejected`() {
        val pickList = PickList()
        pickList.addLine(Sku("SKU-1"), Quantity(1))

        assertThatThrownBy { pickList.markPicked(Sku("UNKNOWN")) }
            .isInstanceOf(IllegalArgumentException::class.java)
        pickList.markPicked(Sku("SKU-1"))
        assertThatThrownBy { pickList.markPicked(Sku("SKU-1")) }
            .isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `only non-empty list with every line picked can complete`() {
        val empty = PickList()
        assertThatThrownBy { empty.complete() }.isInstanceOf(IllegalStateException::class.java)

        val pickList = PickList()
        pickList.addLine(Sku("SKU-1"), Quantity(1))
        assertThatThrownBy { pickList.complete() }.isInstanceOf(IllegalStateException::class.java)

        pickList.markPicked(Sku("SKU-1"))
        pickList.complete()
        assertThat(pickList.status).isEqualTo(PickListStatus.COMPLETED)
        assertThatThrownBy { pickList.addLine(Sku("SKU-2"), Quantity(1)) }
            .isInstanceOf(IllegalStateException::class.java)
    }
}
