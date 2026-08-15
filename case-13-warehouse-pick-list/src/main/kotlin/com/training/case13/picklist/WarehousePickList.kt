package com.training.case13.picklist

@JvmInline
value class Sku(val value: String)

@JvmInline
value class Quantity(val value: Int)

enum class PickListStatus {
 OPEN,
 COMPLETED
}

data class PickLine(val sku: Sku, val quantity: Quantity, val picked: Boolean)

class PickList {
 fun addLine(sku: Sku, quantity: Quantity) {
 TODO("Implement add-line invariants for quantity validity, duplicate SKU policy, and status constraints")
 }

 fun markPicked(sku: Sku) {
 TODO("Implement picked transition with explicit behavior for unknown SKU and completed lists")
 }

 fun complete() {
 TODO("Implement completion rules so only non-empty lists with all picked lines can complete")
 }
}
