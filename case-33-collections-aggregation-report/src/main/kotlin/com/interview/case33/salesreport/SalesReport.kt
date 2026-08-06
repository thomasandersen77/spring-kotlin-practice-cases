package com.interview.case33.salesreport

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * COLLECTIONS OG AGGREGERING
 *
 * Ordre med ordrelinjer inn, rapporttall ut.
 *
 * Felles regel for hele caset: KANSELLERTE ORDRE TELLER IKKE i noen omsetning eller salgstall.
 * Beløp er i øre (Long) — ingen `Double` i pengeberegninger.
 *
 * Se README for læringsmål og akseptansekriterier.
 */

enum class OrderStatus {
    PLACED,
    SHIPPED,
    CANCELLED
}

data class OrderLine(
    val sku: String,
    val productName: String,
    val category: String,
    val quantity: Int,
    val unitPriceOre: Long
)

data class Order(
    val id: String,
    val customerId: String,
    val placedAt: LocalDateTime,
    val status: OrderStatus,
    val lines: List<OrderLine>
)

data class SkuSales(
    val sku: String,
    val unitsSold: Int,
    val revenueOre: Long
)

data class DailyRevenue(
    val date: LocalDate,
    val revenueOre: Long,
    val cumulativeRevenueOre: Long
)

// ---------- Aggregeringer (din jobb) ----------

/**
 * Regel 1: Extension property: `quantity * unitPriceOre`.
 */
val OrderLine.lineTotalOre: Long
    get() = quantity * unitPriceOre

/**
 * Regel 2: Summen av alle linjene i ordren med `sumOf`.
 */
fun Order.totalOre(): Long =
    lines.sumOf(OrderLine::lineTotalOre)

/**
 * Regel 3: Alle ordre som ikke er kansellert.
 */
fun List<Order>.excludingCancelled(): List<Order> =
    filter { order -> order.status != OrderStatus.CANCELLED }

/**
 * Regel 4: Omsetning per kategori.
 * Kontrakt: nøklene skal komme i alfabetisk rekkefølge (rekkefølgen i Map-en er en del av kontrakten).
 * Hint: `flatMap`, `groupBy`, `mapValues`, `sumOf`, `toSortedMap`/`sortedBy`.
 */
fun List<Order>.revenuePerCategory(): Map<String, Long> =
    sellableLines()
        .groupBy(OrderLine::category)
        .mapValues { (_, lines) -> lines.sumOf(OrderLine::lineTotalOre) }
        .toSortedMap()

/**
 * Regel 5: Bestselgere.
 * Kontrakt: aggregér per SKU (antall solgte og omsetning), sorter på antall desc og deretter sku asc,
 * og returner maks `limit` rader.
 * Hint: `flatMap`, `groupBy`, `map`, `sortedWith(compareByDescending<...>{ }.thenBy { })`, `take`.
 */
fun List<Order>.topSellingSkus(limit: Int): List<SkuSales> {
    require(limit >= 0) { "limit cannot be negative" }

    return sellableLines()
        .groupBy(OrderLine::sku)
        .map { (sku, lines) ->
            SkuSales(
                sku = sku,
                unitsSold = lines.sumOf(OrderLine::quantity),
                revenueOre = lines.sumOf(OrderLine::lineTotalOre)
            )
        }
        .sortedWith(
            compareByDescending<SkuSales>(SkuSales::unitsSold)
                .thenBy(SkuSales::sku)
        )
        .take(limit)
}

/**
 * Regel 6: Antall ordre per kunde (kansellerte teller ikke).
 * Kunder uten tellende ordre skal ikke finnes i Map-en.
 * Hint: `groupingBy { }.eachCount()`.
 */
fun List<Order>.orderCountPerCustomer(): Map<String, Int> =
    excludingCancelled()
        .groupingBy(Order::customerId)
        .eachCount()

/**
 * Regel 7: Kunden med høyest omsetning, eller `null` hvis ingen tellende ordre finnes.
 * Ved likt beløp vinner kunde-id-en som kommer først alfabetisk.
 */
fun List<Order>.bestCustomerByRevenue(): String? =
    excludingCancelled()
        .groupBy(Order::customerId)
        .mapValues { (_, orders) -> orders.sumOf(Order::totalOre) }
        .entries
        .sortedWith(
            compareByDescending<Map.Entry<String, Long>> { it.value }
                .thenBy { it.key }
        )
        .firstOrNull()
        ?.key

/**
 * Regel 8: Del ordrene i to: `first` = kansellerte, `second` = resten. Rekkefølgen bevares.
 * Hint: `partition`.
 */
fun List<Order>.splitByCancellation(): Pair<List<Order>, List<Order>> =
    partition { order -> order.status == OrderStatus.CANCELLED }

/**
 * Regel 9: Gjennomsnittlig ordreverdi i øre, eller `null` når det ikke finnes tellende ordre.
 * Hint: `map`, `average`, `takeIf` — og tenk gjennom hvorfor `average()` på tom liste gir `NaN`.
 */
fun List<Order>.averageOrderValueOre(): Double? {
    val orders = excludingCancelled()
    return orders
        .takeIf(List<Order>::isNotEmpty)
        ?.map(Order::totalOre)
        ?.average()
}

/**
 * Regel 10: Omsetning per dag med løpende sum.
 * Kontrakt: én rad per dag som har tellende ordre, sortert på dato asc; `cumulativeRevenueOre`
 * er summen til og med den dagen. Dager uten ordre skal ikke gi rader.
 * Hint: `groupBy { it.placedAt.toLocalDate() }`, `toSortedMap`/`sortedBy`, `runningFold`/`scan`.
 */
fun List<Order>.dailyRevenue(): List<DailyRevenue> {
    val revenueByDate = excludingCancelled()
        .groupBy { order -> order.placedAt.toLocalDate() }
        .toSortedMap()
        .map { (date, orders) -> date to orders.sumOf(Order::totalOre) }

    val cumulativeRevenue = revenueByDate
        .runningFold(0L) { accumulated, (_, revenue) -> accumulated + revenue }
        .drop(1)

    return revenueByDate.zip(cumulativeRevenue) { (date, revenue), cumulative ->
        DailyRevenue(
            date = date,
            revenueOre = revenue,
            cumulativeRevenueOre = cumulative
        )
    }
}

/**
 * Regel 11: Hvilke kunder har kjøpt hvilke SKU-er?
 * Kontrakt: SKU -> mengde av kunde-id-er (tellende ordre).
 * Hint: `flatMap` over ordre og linjer, `groupBy`, `mapValues { it.value.map { ... }.toSet() }`.
 */
fun List<Order>.customersPerSku(): Map<String, Set<String>> =
    excludingCancelled()
        .flatMap { order -> order.lines.map { line -> line.sku to order.customerId } }
        .groupBy(keySelector = { it.first }, valueTransform = { it.second })
        .mapValues { (_, customerIds) -> customerIds.toSet() }

private fun List<Order>.sellableLines(): List<OrderLine> =
    excludingCancelled().flatMap(Order::lines)
