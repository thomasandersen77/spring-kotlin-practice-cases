package com.interview.case51.comparison

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FrameworkComparisonTest {

    @Test
    fun `matrisen beskriver alle tre rammeverk uten duplikater`() {
        assertThat(frameworkProfiles().map { it.framework })
            .containsExactlyInAnyOrder(WebFramework.SPRING_BOOT, WebFramework.HTTP4K, WebFramework.KTOR)
    }

    @Test
    fun `spring velges pragmatisk ved JPA method security og eksisterende kompetanse`() {
        val recommendation = recommend(
            Requirements(
                needsJpaAndMethodSecurity = true,
                prefersFunctionalComposition = false,
                coroutineFirst = false,
                teamSpringExperience = true
            )
        )

        assertThat(recommendation.framework).isEqualTo(WebFramework.SPRING_BOOT)
        assertThat(recommendation.reasons).isNotEmpty()
        assertThat(recommendation.rejectedAlternatives).containsKeys(WebFramework.HTTP4K, WebFramework.KTOR)
    }

    @Test
    fun `anbefalingen kan forsvare kotlin native alternativer`() {
        assertThat(
            recommend(Requirements(false, true, false, false)).framework
        ).isEqualTo(WebFramework.HTTP4K)
        assertThat(
            recommend(Requirements(false, false, true, false)).framework
        ).isEqualTo(WebFramework.KTOR)
    }
}
