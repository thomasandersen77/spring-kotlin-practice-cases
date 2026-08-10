package com.interview.case51.comparison

enum class WebFramework { SPRING_BOOT, HTTP4K, KTOR }

data class Requirements(
    val needsJpaAndMethodSecurity: Boolean,
    val prefersFunctionalComposition: Boolean,
    val coroutineFirst: Boolean,
    val teamSpringExperience: Boolean
)

data class FrameworkProfile(
    val framework: WebFramework,
    val dependencyInjection: String,
    val middlewareModel: String,
    val coroutineSupport: String,
    val tradeOff: String
)

data class Recommendation(
    val framework: WebFramework,
    val reasons: List<String>,
    val rejectedAlternatives: Map<WebFramework, String>
)

// TODO 1: Beskriv hver stack presist uten markedsføringsspråk.
fun frameworkProfiles(): List<FrameworkProfile> =
    TODO("Bygg sammenligningsmatrise")

// TODO 2: Velg pragmatisk stack ut fra krav og gjør trade-offs eksplisitte.
fun recommend(requirements: Requirements): Recommendation =
    TODO("Implementer og begrunn anbefaling")
