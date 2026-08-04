package com.interview.case33.salesreport

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class SalesReportTest {

    private val tv = OrderLine("TV-55", "TV 55 tommer", "Elektronikk", quantity = 1, unitPriceOre = 899_900)
    private val hdmiTwo = OrderLine("HDMI", "HDMI-kabel", "Elektronikk", quantity = 2, unitPriceOre = 19_900)
    private val hdmiThree = OrderLine("HDMI", "HDMI-kabel", "Elektronikk", quantity = 3, unitPriceOre = 19_900)
    private val socksFive = OrderLine("SOKK", "Ullsokker", "Klær", quantity = 5, unitPriceOre = 14_900)
    private val socksTwo = OrderLine("SOKK", "Ullsokker", "Klær", quantity = 2, unitPriceOre = 14_900)
    private val mouseOne = OrderLine("MUS", "Trådløs mus", "Elektronikk", quantity = 1, unitPriceOre = 49_900)
    private val mouseFour = OrderLine("MUS", "Trådløs mus", "Elektronikk", quantity = 4, unitPriceOre = 49_900)

    private val order1 = Order(
        id = "O-1",
        customerId = "C-1",
        placedAt = LocalDateTime.of(2026, 7, 1, 10, 0),
        status = OrderStatus.PLACED,
        lines = listOf(tv, hdmiTwo)
    )
    private val order2 = Order(
        id = "O-2",
        customerId = "C-2",
        placedAt = LocalDateTime.of(2026, 7, 1, 14, 0),
        status = OrderStatus.SHIPPED,
        lines = listOf(hdmiThree, socksFive)
    )
    private val order3 = Order(
        id = "O-3",
        customerId = "C-1",
        placedAt = LocalDateTime.of(2026, 7, 2, 9, 0),
        status = OrderStatus.SHIPPED,
        lines = listOf(socksTwo, mouseOne)
    )
    private val cancelledOrder = Order(
        id = "O-4",
        customerId = "C-3",
        placedAt = LocalDateTime.of(2026, 7, 2, 11, 0),
        status = OrderStatus.CANCELLED,
        lines = listOf(tv.copy(quantity = 10))
    )
    private val order5 = Order(
        id = "O-5",
        customerId = "C-2",
        placedAt = LocalDateTime.of(2026, 7, 4, 8, 0),
        status = OrderStatus.PLACED,
        lines = listOf(mouseFour)
    )

    private val orders = listOf(order1, order2, order3, cancelledOrder, order5)

    @Test
    fun `linjetotal er antall ganger enhetspris`() {
        assertThat(hdmiTwo.lineTotalOre).isEqualTo(39_800L)
        assertThat(tv.lineTotalOre).isEqualTo(899_900L)
    }

    @Test
    fun `ordretotal summerer linjene`() {
        assertThat(order1.totalOre()).isEqualTo(939_700L)
        assertThat(order3.totalOre()).isEqualTo(79_700L)
        assertThat(order1.copy(lines = emptyList()).totalOre()).isZero()
    }

    @Test
    fun `kansellerte ordre filtreres bort`() {
        assertThat(orders.excludingCancelled().map { it.id })
            .containsExactly("O-1", "O-2", "O-3", "O-5")
    }

    @Test
    fun `omsetning per kategori er sortert alfabetisk og utelater kansellert salg`() {
        val revenue = orders.revenuePerCategory()

        assertThat(revenue.keys).containsExactly("Elektronikk", "Klær")
        assertThat(revenue["Elektronikk"]).isEqualTo(1_248_900L)
        assertThat(revenue["Klær"]).isEqualTo(104_300L)
    }

    @Test
    fun `bestselgere sorteres pa antall og deretter sku`() {
        assertThat(orders.topSellingSkus(limit = 3)).containsExactly(
            SkuSales("SOKK", unitsSold = 7, revenueOre = 104_300),
            SkuSales("HDMI", unitsSold = 5, revenueOre = 99_500),
            SkuSales("MUS", unitsSold = 5, revenueOre = 249_500)
        )
    }

    @Test
    fun `bestselgerlisten begrenses av limit og utelater kansellert salg`() {
        val top = orders.topSellingSkus(limit = 1)

        assertThat(top).containsExactly(SkuSales("SOKK", unitsSold = 7, revenueOre = 104_300))
        assertThat(orders.topSellingSkus(limit = 10).map { it.sku })
            .containsExactly("SOKK", "HDMI", "MUS", "TV-55")
    }

    @Test
    fun `ordreantall per kunde utelater kunder som bare har kansellerte ordre`() {
        val counts = orders.orderCountPerCustomer()

        assertThat(counts).containsOnlyKeys("C-1", "C-2")
        assertThat(counts["C-1"]).isEqualTo(2)
        assertThat(counts["C-2"]).isEqualTo(2)
    }

    @Test
    fun `beste kunde er den med hoyest omsetning`() {
        assertThat(orders.bestCustomerByRevenue()).isEqualTo("C-1")
    }

    @Test
    fun `ved likt belop vinner kunde-id-en som kommer forst alfabetisk`() {
        val sameRevenue = listOf(
            order1.copy(id = "O-10", customerId = "C-9"),
            order1.copy(id = "O-11", customerId = "C-2")
        )

        assertThat(sameRevenue.bestCustomerByRevenue()).isEqualTo("C-2")
    }

    @Test
    fun `partisjonering skiller kansellerte fra resten og bevarer rekkefolgen`() {
        val (cancelled, active) = orders.splitByCancellation()

        assertThat(cancelled.map { it.id }).containsExactly("O-4")
        assertThat(active.map { it.id }).containsExactly("O-1", "O-2", "O-3", "O-5")
    }

    @Test
    fun `gjennomsnittlig ordreverdi regnes bare av tellende ordre`() {
        assertThat(orders.averageOrderValueOre()).isEqualTo(338_300.0)
    }

    @Test
    fun `daglig omsetning har lopende sum og ingen tomme dager`() {
        assertThat(orders.dailyRevenue()).containsExactly(
            DailyRevenue(LocalDate.of(2026, 7, 1), revenueOre = 1_073_900, cumulativeRevenueOre = 1_073_900),
            DailyRevenue(LocalDate.of(2026, 7, 2), revenueOre = 79_700, cumulativeRevenueOre = 1_153_600),
            DailyRevenue(LocalDate.of(2026, 7, 4), revenueOre = 199_600, cumulativeRevenueOre = 1_353_200)
        )
    }

    @Test
    fun `kunder per sku samler unike kunder`() {
        val customers = orders.customersPerSku()

        assertThat(customers).containsOnlyKeys("TV-55", "HDMI", "SOKK", "MUS")
        assertThat(customers["TV-55"]).containsExactly("C-1")
        assertThat(customers["HDMI"]).containsExactlyInAnyOrder("C-1", "C-2")
        assertThat(customers["SOKK"]).containsExactlyInAnyOrder("C-1", "C-2")
        assertThat(customers["MUS"]).containsExactlyInAnyOrder("C-1", "C-2")
    }

    @Test
    fun `tom ordreliste gir tomme rapporter og null der det ikke finnes tall`() {
        val empty = emptyList<Order>()

        assertThat(empty.revenuePerCategory()).isEmpty()
        assertThat(empty.topSellingSkus(limit = 5)).isEmpty()
        assertThat(empty.orderCountPerCustomer()).isEmpty()
        assertThat(empty.customersPerSku()).isEmpty()
        assertThat(empty.dailyRevenue()).isEmpty()
        assertThat(empty.bestCustomerByRevenue()).isNull()
        assertThat(empty.averageOrderValueOre()).isNull()
    }

    @Test
    fun `bare kansellerte ordre gir samme resultat som ingen ordre`() {
        val onlyCancelled = listOf(cancelledOrder)

        assertThat(onlyCancelled.revenuePerCategory()).isEmpty()
        assertThat(onlyCancelled.topSellingSkus(limit = 5)).isEmpty()
        assertThat(onlyCancelled.bestCustomerByRevenue()).isNull()
        assertThat(onlyCancelled.averageOrderValueOre()).isNull()
        assertThat(onlyCancelled.dailyRevenue()).isEmpty()
    }
}
