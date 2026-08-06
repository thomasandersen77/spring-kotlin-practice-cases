package com.interview.case13.picklist

@JvmInline
value class Sku(val value: String) {
    init { require(value.isNotBlank()) { "sku cannot be blank" } }
}

@JvmInline
value class Quantity(val value: Int) {
    init { require(value > 0) { "quantity must be positive" } }
}

enum class PickListStatus {
    OPEN,
    COMPLETED
}

data class PickLine(val sku: Sku, val quantity: Quantity, val picked: Boolean)

class PickList {
    private val mutableLines = linkedMapOf<Sku, PickLine>()
    val lines: List<PickLine> get() = mutableLines.values.toList()
    var status: PickListStatus = PickListStatus.OPEN
        private set

    fun addLine(sku: Sku, quantity: Quantity) {
        ensureOpen()
        require(sku !in mutableLines) { "sku already exists in pick list" }
        mutableLines[sku] = PickLine(sku, quantity, picked = false)
    }

    fun markPicked(sku: Sku) {
        ensureOpen()
        val line = requireNotNull(mutableLines[sku]) { "unknown sku: ${sku.value}" }
        check(!line.picked) { "sku is already picked" }
        mutableLines[sku] = line.copy(picked = true)
    }

    fun complete() {
        ensureOpen()
        check(mutableLines.isNotEmpty()) { "empty pick list cannot be completed" }
        check(mutableLines.values.all(PickLine::picked)) { "all lines must be picked" }
        status = PickListStatus.COMPLETED
    }

    private fun ensureOpen() = check(status == PickListStatus.OPEN) { "pick list is completed" }
}
