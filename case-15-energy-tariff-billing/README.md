# Case 15 - Energy tariff billing

## Domene
Strømavregning

## Tid
60 minutter

## Hva dette trener
- Value Objects
- Precision
- Rounding
- BigDecimal-regler

## Scenario
En strømregning skal beregnes fra målerstand, energitariff og mva. Testen viser bare at en enkel beregning skal gi positiv total.

## Oppgave
Implementer beregning av forbruk, variable kostnader, fastledd, mva og avrunding på en måte som er presis og lesbar.

## TODO / fokusområder
- TODO: Beregn kWh som differansen mellom slutt- og startmåling, og valider at måleren ikke går bakover.
- TODO: Kombiner spotpris, nettleie/grid og fast månedsbeløp med tydelig rekkefølge.
- TODO: Avklar om mva gjelder alle komponenter eller bare deler av regningen.
- TODO: Velg avrunding/skala for sluttbeløp og legg tester rundt øredifferanser.
- TODO: Vurder små value objects for kWh/prosent/penger hvis det gjør koden tydeligere, ikke bare mer abstrakt.

## Akseptansekriterier
- En enkel regning kan etterregnes fra testdata.
- Negativt forbruk og ugyldig mva håndteres bevisst.
- `BigDecimal` brukes uten binære flyttallsfeil.
- Beregningsstegene er tydelige nok til å reviewes.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke nøy deg med `assertThat(total).isPositive`. Skriv minst én test med forventet konkret total og én kanttest.

## Kommandoer

```bash
mvn -pl case-15-energy-tariff-billing test
```
