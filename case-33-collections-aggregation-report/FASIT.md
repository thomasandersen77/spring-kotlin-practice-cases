# Fasit – Case 33

## Oppgaver og kontrakt

Alle elleve aggregeringer er implementert. Kansellerte ordre filtreres ut av
alle omsetnings- og salgstall, men beholdes i partisjoneringsfunksjonen.
Rapportene har deterministisk sortering, tom input gir tomme strukturer, og
fraværende numeriske resultater uttrykkes som `null`. Beløp beregnes som `Long`
i øre.

## Valgt løsning og Kotlin-konsepter

`lineTotalOre` og `totalOre` er små gjenbrukbare byggeklosser.
`excludingCancelled` og `sellableLines` samler fellesreglene, slik at de lange
aggregeringene ikke dupliserer filtrering.

Løsningen bruker `filter`, `flatMap`, `sumOf`, `groupBy`, `mapValues`,
`groupingBy().eachCount()`, `partition`, `sortedWith`, `take`, `toSet` og
`runningFold`. Daglig omsetning bygges som sorterte `(dato, beløp)`-par, en
read-only liste av løpende summer og til slutt `zip` til DTO-er. Ingen
for-løkker, `var` eller mutable collections brukes.

## Edge cases og teststrategi

Testene dekker kansellering, tomme ordrelinjer og ordrelister, bare kansellerte
ordre, lik kundesomsetning, likt antall solgte enheter, alfabetiske sekundær-
sorteringer, unike kunder per SKU, manglende dager og løpende sum. En
tilleggstest dokumenterer at negativ `limit` avvises.

## Designvalg, alternativer og trade-offs

`groupBy` brukes når de grupperte elementene trengs for videre summering;
`groupingBy().eachCount()` brukes når bare antallet trengs. Det unngår en liste
per kunde i ordreopptellingen. Vanlige eager collections prioriteres for
lesbarhet i casets datamengde. For millioner av ordre ville rapportering normalt
flyttes til databasen eller en stream-/batchprosess; `Sequence` alene løser ikke
minnebehovet i operasjoner som fortsatt må gruppere og sortere alt.

`average()` brukes først etter eksplisitt tomsjekk, slik at `NaN` ikke lekker
inn i domenekontrakten. `Double` er bare rapportresultatet for gjennomsnittet;
alle pengebeløp summeres eksakt som `Long` før konverteringen.

## Kort intervjuforklaring

«Jeg trakk kanselleringsregelen og linjeflatingen ut i navngitte helpers. Så
valgte jeg collection-operasjon etter resultatbehovet: `groupingBy` for telling,
`groupBy` for summering, `partition` for todeling og `runningFold` for
historikken. Alle tie-breakere og tomtilfeller er eksplisitte.»
