# Case 35 - DSL og higher-order functions

## Domene
Konsulentsøk / bemanning

## Tid
60-75 minutter

## Hva dette trener
Læringsmål for dette caset:
- Funksjonstyper og `typealias`: at et søkekriterium *er* en funksjon (`(Consultant) -> Boolean`)
- Higher-order functions: funksjoner som tar og returnerer funksjoner, med closures over parametre
- Lambda with receiver (`Builder.() -> Unit`) — grunnmuren i alle Kotlin-DSL-er
- Trailing lambda-syntaks og nøstede DSL-blokker (`anyOf { }`)
- `infix`-funksjoner og operatoroverlasting (`and`, `or`, `!`)
- `vararg` som predikatfabrikk
- Delegert property med `by lazy` — dyre data lastes én gang, og bare ved behov
- `buildString` og string templates til deterministisk tekstbygging

## Scenario
Bemanningsavdelingen søker i konsulentdatabasen med stadig nye kombinasjoner av krav. I stedet for en `findByCityAndSkillAndRate...`-eksplosjon skal du bygge et lite, lesbart søke-API:

```kotlin
val treff = consultants.search {
    skill("Kotlin")
    availableOnly()
    anyOf {
        inCity("Oslo")
        maxHourlyRate(1200)
    }
}
```

Hele `ConsultantSearchDsl.kt` er uløst (`TODO()`). Testene beskriver kontrakten.

## Oppgave
Bygg DSL-en, predikatkomposisjonen, den lazy indeksen og rapportbyggeren. Kriterier på toppnivå kombineres med AND, `anyOf { }` er en OR-gruppe, og et tomt søk matcher alle.

## TODO / fokusområder
- TODO 1: Velg intern representasjon i `ConsultantSearchBuilder` (typisk en privat liste av predikater). Ingen intern tilstand skal lekke ut.
- TODO 2-6: `skill`, `inCity`, `maxHourlyRate`, `minYearsOfExperience`, `availableOnly` — eksakt match på tekst, `<=` på pris, `>=` på erfaring.
- TODO 7: `anyOf { }` — OR internt, AND mot resten. Tom blokk matcher alle. Bruk en ny builder og lambda with receiver.
- TODO 8: `build()` — ingen kriterier = matcher alle.
- TODO 9: `consultantSearch { }` som inngangspunkt.
- TODO 10: `List<Consultant>.search { }` — bevar rekkefølgen fra kildelisten.
- TODO 11-13: `and`, `or` som `infix`, og `not` som `operator` slik at `!predikat` fungerer.
- TODO 14: `hasAllSkills(vararg skills)` — ingen ferdigheter oppgitt matcher alle.
- TODO 15: `ConsultantIndex` — bytt `get() = TODO(...)` til `by lazy` slik at `load` kalles maks én gang, og bare når indeksen brukes. `withSkill` gir tom liste for ukjent ferdighet.
- TODO 16: `List<Consultant>.toReport(title)` med `buildString` — eksakt format står i KDoc-en og testen.
- TODO 17: Vurder `@DslMarker` på builderen. Hva løser den, og trengs den her?

## Akseptansekriterier
- Alle tester i `ConsultantSearchDslTest` er grønne.
- DSL-en er lesbar: kallstedet i testen skal se ut som en spesifikasjon, ikke som en konfigurasjonsklasse.
- Ingen mutable tilstand er synlig utenfor `ConsultantSearchBuilder`.
- `ConsultantIndex.load` kalles nøyaktig én gang selv om flere medlemmer brukes, og null ganger hvis ingen brukes.
- Ingen `!!`, ingen `for`-løkker, ingen `var` i offentlig API.
- Rapporten er deterministisk (sortert) og har ikke linjeskift på slutten.

## Formål i intervjuet
Kotlin-DSL-er er det tydeligste skillet mellom "kan syntaksen" og "forstår språket": lambda with receiver, funksjonstyper, `infix`, operatoroverlasting og delegering i én liten øvelse. Samtidig er dette et sted man lett overdriver — så caset ber deg også vurdere når et DSL er verdt kompleksiteten, og når en enkel data class med felter er et bedre valg.

## Ikke gjør det for lett
Ikke la builderen returnere `this` og bygge en fluent chain — poenget er lambda with receiver. Ikke gjør `anyOf` til en spesialvariabel i builderen; den skal komponere predikater. Og ikke gjør indeksen `lateinit var` med en `init`-blokk — den skal være lazy.

## Intervjuspørsmål / debrief
1. Hva er forskjellen på `(T) -> Unit` og `T.() -> Unit`, og hvorfor er den viktig for DSL-er?
2. Hva gjør `@DslMarker`, og hvilken feil forhindrer den?
3. `by lazy` vs. `lateinit var` vs. initialisering i konstruktøren — hva velger du når, og hva med trådsikkerhet?
4. Når er `infix` og operatoroverlasting god lesbarhet, og når er det bare smart?
5. Hva koster higher-order functions på JVM-en, og hva gjør `inline` med det?
6. Dette søket kjøres i minnet. Hvordan ville du oversatt samme DSL til et databasespørring (Specification/Criteria API), og hva blir vanskelig?
7. Når ville du valgt en enkel `SearchCriteria`-data class fremfor et DSL?

## Kommandoer

```bash
./mvnw test -pl case-35-kotlin-dsl-higher-order-functions
```
