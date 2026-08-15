package com.training.case33.salesreport

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * COLLECTIONS OG AGGREGERING
 *
 * Ordre med ordrelinjer inn, rapporttall ut. Alle aggregeringene er uløste (`TODO()`).
 *
 * Felles regel for hele caset: KANSELLERTE ORDRE TELLER IKKE i noen omsetning eller salgstall.
 * Beløp er i øre (Long) — ingen `Double` i pengeberegninger.
 *
 * Se README for TODO-liste, læringsmål og akseptansekriterier.
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
 * TODO 1: Extension property: `quantity * unitPriceOre`.
 */
val OrderLine.lineTotalOre: Long
 get() = TODO("Implementer linjetotal som extension property")

/**
 * TODO 2: Summen av alle linjene i ordren. Hint: `sumOf`.
 */
fun Order.totalOre(): Long =
 TODO("Implementer ordretotal")

/**
 * TODO 3: Alle ordre som ikke er kansellert.
 */
fun List<Order>.excludingCancelled(): List<Order> =
 TODO("Filtrer bort kansellerte ordre")

/**
 * TODO 4: Omsetning per kategori.
 * Kontrakt: nøklene skal komme i alfabetisk rekkefølge (rekkefølgen i Map-en er en del av kontrakten).
 * Hint: `flatMap`, `groupBy`, `mapValues`, `sumOf`, `toSortedMap`/`sortedBy`.
 */
fun List<Order>.revenuePerCategory(): Map<String, Long> =
 TODO("Implementer omsetning per kategori")

/**
 * TODO 5: Bestselgere.
 * Kontrakt: aggregér per SKU (antall solgte og omsetning), sorter på antall desc og deretter sku asc,
 * og returner maks `limit` rader.
 * Hint: `flatMap`, `groupBy`, `map`, `sortedWith(compareByDescending<...>{ }.thenBy { })`, `take`.
 */
fun List<Order>.topSellingSkus(limit: Int): List<SkuSales> =
 TODO("Implementer bestselgerliste")

/**
 * TODO 6: Antall ordre per kunde (kansellerte teller ikke).
 * Kunder uten tellende ordre skal ikke finnes i Map-en.
 * Hint: `groupingBy { }.eachCount()`.
 */
fun List<Order>.orderCountPerCustomer(): Map<String, Int> =
 TODO("Implementer ordreantall per kunde")

/**
 * TODO 7: Kunden med høyest omsetning, eller `null` hvis ingen tellende ordre finnes.
 * Ved likt beløp vinner kunde-id-en som kommer først alfabetisk.
 */
fun List<Order>.bestCustomerByRevenue(): String? =
 TODO("Implementer beste kunde")

/**
 * TODO 8: Del ordrene i to: `first` = kansellerte, `second` = resten. Rekkefølgen bevares.
 * Hint: `partition`.
 */
fun List<Order>.splitByCancellation(): Pair<List<Order>, List<Order>> =
 TODO("Implementer partisjonering på kansellering")

/**
 * TODO 9: Gjennomsnittlig ordreverdi i øre, eller `null` når det ikke finnes tellende ordre.
 * Hint: `map`, `average`, `takeIf` — og tenk gjennom hvorfor `average()` på tom liste gir `NaN`.
 */
fun List<Order>.averageOrderValueOre(): Double? =
 TODO("Implementer gjennomsnittlig ordreverdi")

/**
 * TODO 10: Omsetning per dag med løpende sum.
 * Kontrakt: én rad per dag som har tellende ordre, sortert på dato asc; `cumulativeRevenueOre`
 * er summen til og med den dagen. Dager uten ordre skal ikke gi rader.
 * Hint: `groupBy { it.placedAt.toLocalDate() }`, `toSortedMap`/`sortedBy`, `runningFold`/`scan`.
 */
fun List<Order>.dailyRevenue(): List<DailyRevenue> =
 TODO("Implementer daglig omsetning med løpende sum")

/**
 * TODO 11: Hvilke kunder har kjøpt hvilke SKU-er?
 * Kontrakt: SKU -> mengde av kunde-id-er (tellende ordre).
 * Hint: `flatMap` over ordre og linjer, `groupBy`, `mapValues { it.value.map { ... }.toSet() }`.
 */
fun List<Order>.customersPerSku(): Map<String, Set<String>> =
 TODO("Implementer kunder per SKU")
