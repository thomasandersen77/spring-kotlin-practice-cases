# Case 33 - Collections og aggregering

## Domene
Netthandel / salgsrapport

## Tid
50-70 minutter

## Hva dette trener
Læringsmål for dette caset:
- Collections-API-et i bredden: `filter`, `map`, `flatMap`, `sumOf`, `groupBy`, `mapValues`
- `groupingBy { }.eachCount()` — og hvorfor det ikke er det samme som `groupBy { }.mapValues { it.value.size }`
- Sortering med flere nøkler: `sortedWith(compareByDescending<T> { }.thenBy { })`
- `partition`, `take`, `distinct`, `associate`, `maxByOrNull`, `minOfOrNull`
- Løpende summer med `runningFold`/`scan` i stedet for mutable akkumulator
- Trygg håndtering av tomme lister: `null` fremfor `NaN` og `0`
- Deterministiske rapporter: rekkefølge og sortering som del av kontrakten
- Penger som `Long` i øre — ikke `Double`

## Scenario
En rapportmodul skal svare på hverdagslige spørsmål fra en netthandel: hva selger best, hvem er beste kunde, hva er omsetningen per kategori og per dag? Ordrene har linjer, og noen ordre er kansellert.

**Felles regel:** kansellerte ordre teller ikke i omsetning eller salgstall. Alle funksjonene i `SalesReport.kt` er uløste (`TODO()`), og testene beskriver kontrakten.

## Oppgave
Implementer aggregeringene med collections-API-et. Ingen `for`-løkker, ingen `var`-akkumulatorer, ingen mutable lister eller maps.

## TODO / fokusområder
- TODO 1: `OrderLine.lineTotalOre` som extension property (`quantity * unitPriceOre`).
- TODO 2: `Order.totalOre()` med `sumOf`.
- TODO 3: `List<Order>.excludingCancelled()`.
- TODO 4: `revenuePerCategory()` — omsetning per kategori, nøkler i alfabetisk rekkefølge.
- TODO 5: `topSellingSkus(limit)` — aggregér per SKU, sorter på antall desc og deretter sku asc, begrens med `limit`.
- TODO 6: `orderCountPerCustomer()` med `groupingBy { }.eachCount()`; kunder uten tellende ordre skal ikke være med.
- TODO 7: `bestCustomerByRevenue()` — `null` når det ikke finnes tellende ordre; alfabetisk lavest kunde-id vinner ved likt beløp.
- TODO 8: `splitByCancellation()` med `partition` — `first` = kansellerte, `second` = resten.
- TODO 9: `averageOrderValueOre()` — `null` i stedet for `NaN` på tom liste.
- TODO 10: `dailyRevenue()` — én rad per dag med ordre, sortert på dato, med løpende sum via `runningFold`/`scan`.
- TODO 11: `customersPerSku()` — `Map<String, Set<String>>` med `flatMap` + `groupBy` + `mapValues`.
- TODO 12: Vurder om noen av kjedene bør bruke `asSequence()`, og vær klar til å begrunne svaret for 10 ordre vs. 10 millioner ordre.

## Akseptansekriterier
- Alle tester i `SalesReportTest` er grønne.
- Ingen `for`-løkker, ingen `var`, ingen mutable collections.
- Kansellerte ordre påvirker ikke noe tall utenom `splitByCancellation`.
- Tom liste gir tomme rapporter, og `null` (ikke `0` eller `NaN`) der et tall ikke finnes.
- Ingen kjede er så lang at du ikke kan forklare den linje for linje — del opp med navngitte hjelpefunksjoner der det trengs.

## Formål i treningen
Aggregering over lister er den vanligste live-koding-oppgaven som finnes, og den avslører raskt om du kan collections-API-et eller om du faller tilbake på løkker og `HashMap`. Coachen ser også etter om du tenker på determinisme (sorteringsrekkefølge), kanttilfeller (tom liste) og presisjon (øre som `Long`).

## Ikke gjør det for lett
Ikke skriv én uleselig kjede på femten operasjoner per funksjon — del opp med navngitte hjelpefunksjoner eller extension functions. Og ikke jukse deg unna `null`-kontrakten ved å returnere `0` for tom liste; det skjuler informasjon fra den som kaller.

## Treningsspørsmål / debrief
1. Hva er forskjellen på `groupBy` og `groupingBy`, og når betyr den noe i praksis?
2. Når bytter du til `asSequence()`, og hva koster det i lesbarhet?
3. `fold` vs. `runningFold` vs. `reduce` — når bruker du hva?
4. Hvorfor `Long` i øre fremfor `Double` i kroner, og hva skjer med `average()`?
5. Hvordan sikrer du deterministisk rekkefølge i en rapport, og hvorfor er det viktig for testene?
6. Denne rapporten kjører i minnet. Når ville du flyttet aggregeringen til databasen i stedet?

## Kommandoer

```bash
./mvnw test -pl case-33-collections-aggregation-report
```
