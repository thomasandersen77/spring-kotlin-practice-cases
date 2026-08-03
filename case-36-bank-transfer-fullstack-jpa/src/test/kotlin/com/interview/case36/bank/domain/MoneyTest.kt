package com.interview.case36.bank.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MoneyTest {

    @Test
    fun `kroner konverteres eksakt til ore`() {
        assertThat(Money.ofKroner(BigDecimal("250.00")).amountOre).isEqualTo(25_000L)
        assertThat(Money.ofKroner(BigDecimal("0.01")).amountOre).isEqualTo(1L)
        assertThat(Money.ofKroner(BigDecimal("1234.56")).amountOre).isEqualTo(123_456L)
        assertThat(Money.ofKroner(BigDecimal("10")).amountOre).isEqualTo(1_000L)
    }

    @Test
    fun `ore konverteres tilbake til kroner deterministisk`() {
        assertThat(Money.ofOre(25_000).toKroner()).isEqualByComparingTo("250.00")
        assertThat(Money.ofOre(1).toKroner()).isEqualByComparingTo("0.01")
        assertThat(Money.ZERO.toKroner()).isEqualByComparingTo("0.00")
    }

    @Test
    fun `belop med mer enn to desimaler avvises`() {
        assertThatThrownBy { Money.ofKroner(BigDecimal("10.005")) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `negativt belop avvises`() {
        assertThatThrownBy { Money.ofKroner(BigDecimal("-1.00")) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { Money.ofOre(-1) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `null belop er gyldig`() {
        assertThat(Money.ofKroner(BigDecimal.ZERO)).isEqualTo(Money.ZERO)
    }

    @Test
    fun `pluss og minus oppdaterer belopet korrekt`() {
        val sum = Money.ofOre(100) + Money.ofOre(50)
        assertThat(sum.amountOre).isEqualTo(150L)

        val diff = Money.ofOre(100) - Money.ofOre(40)
        assertThat(diff.amountOre).isEqualTo(60L)
    }

    @Test
    fun `minus som ville gitt negativt resultat kastes`() {
        assertThatThrownBy { Money.ofOre(10) - Money.ofOre(20) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `isLessThan sammenligner belop korrekt`() {
        assertThat(Money.ofOre(50).isLessThan(Money.ofOre(100))).isTrue()
        assertThat(Money.ofOre(100).isLessThan(Money.ofOre(100))).isFalse()
        assertThat(Money.ofOre(150).isLessThan(Money.ofOre(100))).isFalse()
    }
}
