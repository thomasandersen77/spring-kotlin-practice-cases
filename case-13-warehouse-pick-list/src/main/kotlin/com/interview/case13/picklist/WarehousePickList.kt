package com.interview.case13.picklist

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
        TODO("Implement add line invariants")
    }

    fun markPicked(sku: Sku) {
        TODO("Implement picked transition")
    }

    fun complete() {
        TODO("Implement completion rules")
    }
}
