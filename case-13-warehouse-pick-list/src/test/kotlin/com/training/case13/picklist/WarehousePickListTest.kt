package com.training.case13.picklist

import org.junit.jupiter.api.Test

class WarehousePickListTest {
	@Test
	fun `should allow adding a valid pick line`() {
		val pickList = PickList()

		pickList.addLine(Sku("SKU-1"), Quantity(2))
	}
}
