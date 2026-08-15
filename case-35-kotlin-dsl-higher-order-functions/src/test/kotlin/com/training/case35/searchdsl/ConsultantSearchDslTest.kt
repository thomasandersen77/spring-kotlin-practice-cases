package com.training.case35.searchdsl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConsultantSearchDslTest {

 private val kari = Consultant(
 name = "Kari",
 city = "Oslo",
 hourlyRateNok = 1600,
 yearsOfExperience = 12,
 skills = setOf("Kotlin", "Spring Boot"),
 available = true
 )
 private val ola = Consultant(
 name = "Ola",
 city = "Bergen",
 hourlyRateNok = 1200,
 yearsOfExperience = 5,
 skills = setOf("Kotlin", "React"),
 available = true
 )
 private val liv = Consultant(
 name = "Liv",
 city = "Oslo",
 hourlyRateNok = 1800,
 yearsOfExperience = 20,
 skills = setOf("Arkitektur", "Kotlin"),
 available = false
 )
 private val per = Consultant(
 name = "Per",
 city = "Oslo",
 hourlyRateNok = 900,
 yearsOfExperience = 1,
 skills = setOf("React"),
 available = true
 )

 private val consultants = listOf(kari, ola, liv, per)

 @Test
 fun `tomt sok matcher alle og bevarer rekkefolgen`() {
 assertThat(consultants.search { }).containsExactly(kari, ola, liv, per)
 }

 @Test
 fun `kriterier pa toppniva kombineres med AND`() {
 val treff = consultants.search {
 inCity("Oslo")
 maxHourlyRate(1600)
 }

 assertThat(treff).containsExactly(kari, per)
 }

 @Test
 fun `availableOnly filtrerer bort utilgjengelige`() {
 val treff = consultants.search {
 inCity("Oslo")
 availableOnly()
 }

 assertThat(treff).containsExactly(kari, per)
 }

 @Test
 fun `ferdighet og erfaring er egne kriterier`() {
 assertThat(consultants.search { skill("Kotlin") }).containsExactly(kari, ola, liv)
 assertThat(consultants.search { minYearsOfExperience(10) }).containsExactly(kari, liv)
 assertThat(consultants.search { skill("COBOL") }).isEmpty()
 }

 @Test
 fun `anyOf er en OR-gruppe som kombineres med AND mot resten`() {
 val treff = consultants.search {
 skill("Kotlin")
 anyOf {
 inCity("Bergen")
 maxHourlyRate(1000)
 }
 }

 assertThat(treff).containsExactly(ola)
 }

 @Test
 fun `tom anyOf-blokk matcher alle`() {
 val treff = consultants.search {
 inCity("Bergen")
 anyOf { }
 }

 assertThat(treff).containsExactly(ola)
 }

 @Test
 fun `predikater kan kombineres med and, or og not`() {
 val inOslo = consultantSearch { inCity("Oslo") }
 val cheap = consultantSearch { maxHourlyRate(1000) }
 val senior = consultantSearch { minYearsOfExperience(10) }

 assertThat(consultants.filter(inOslo and cheap)).containsExactly(per)
 assertThat(consultants.filter(cheap or senior)).containsExactly(kari, liv, per)
 assertThat(consultants.filter(!inOslo)).containsExactly(ola)
 }

 @Test
 fun `hasAllSkills krever alle ferdighetene`() {
 assertThat(consultants.filter(hasAllSkills("Kotlin", "React"))).containsExactly(ola)
 assertThat(consultants.filter(hasAllSkills("Kotlin"))).containsExactly(kari, ola, liv)
 assertThat(consultants.filter(hasAllSkills())).containsExactly(kari, ola, liv, per)
 }

 @Test
 fun `indeksen laster data lazy og bare en gang`() {
 var loadCount = 0
 val index = ConsultantIndex {
 loadCount++
 consultants
 }

 assertThat(loadCount).isZero()

 assertThat(index.bySkill["Kotlin"]).containsExactly(kari, ola, liv)
 assertThat(index.byCity["Oslo"]).containsExactly(kari, liv, per)
 assertThat(index.withSkill("React")).containsExactly(ola, per)

 assertThat(loadCount).isEqualTo(1)
 }

 @Test
 fun `ukjent ferdighet gir tom liste`() {
 val index = ConsultantIndex { consultants }

 assertThat(index.withSkill("COBOL")).isEmpty()
 }

 @Test
 fun `rapporten sorterer pa navn og avslutter med snittpris`() {
 val report = consultants.toReport("Alle konsulenter")

 assertThat(report).isEqualTo(
 """
 Rapport: Alle konsulenter
 - Kari (Oslo), 1600 kr/t, 12 år
 - Liv (Oslo), 1800 kr/t, 20 år
 - Ola (Bergen), 1200 kr/t, 5 år
 - Per (Oslo), 900 kr/t, 1 år
 Snittpris: 1375 kr/t
 """.trimIndent()
 )
 }

 @Test
 fun `snittprisen avrundes til naermeste hele krone`() {
 val report = listOf(kari, ola, per).toReport("Utvalg")

 assertThat(report.lines().last()).isEqualTo("Snittpris: 1233 kr/t")
 }

 @Test
 fun `tom rapport sier ingen treff`() {
 val report = emptyList<Consultant>().toReport("Tomt søk")

 assertThat(report).isEqualTo("Rapport: Tomt søk\n(ingen treff)")
 }
}
