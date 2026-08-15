# Case 01 - Ren Kotlin domenemodell

## Domene
Prising / handlekurv

## Tid
45-60 minutter

## Hva dette trener
- Value Objects
- sealed class og exhaustive `when`
- BigDecimal og avrunding
- Invariants nær konstruktøren
- TDD / domenekontrakt

## Scenario
Du har en handlekurv med linjer, penger og rabatter. Koden har allerede noen gode byggesteiner, men modellen trenger tydeligere invariants og bedre tester rundt rabattreglene.

## Oppgave
Forbedre `PricingDomain.kt` slik at totalsum og rabatter er enkle å forstå, trygge å bruke og lette å utvide. Hold løsningen rammeverksfri.

## TODO / fokusområder
- TODO: Valider `Money`, `BasketLine.quantity` og eventuelle rabattverdier slik at ugyldige tilstander ikke blir normale objekter.
- TODO: Test `NoDiscount`, prosent-rabatt og fast rabatt med både normale beløp og kanttilfeller.
- TODO: Avklar avrunding/skala for `BigDecimal`, men unngå å innføre en stor money-library-løsning.
- TODO: Vurder om rabatt bør være data (`sealed class`) eller strategi, og forklar tradeoff.
- TODO: Sørg for at fast rabatt aldri gir negativ total, men test mer enn akkurat ett eksempel.

## Akseptansekriterier
- Domenet kan testes med vanlige JUnit-tester uten Spring.
- Ugyldige inputverdier stoppes nær der de oppstår.
- Rabattreglene er eksplisitte og enkle å utvide.
- Koden viser idiomatisk Kotlin uten å bli kunstig kompleks.

## Formål i treningen
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste treningsvalg.

## Ikke gjør det for lett
Ikke bare få nåværende tester grønne. Legg til minst én test som tvinger deg til å ta et designvalg rundt validering eller avrunding.

## Treningsspørsmål / debrief
1. Hvorfor er `Money` et Value Object og ikke en `Double` eller `BigDecimal` som flyter fritt?
2. Hvorfor valgte du `sealed class` for `Discount` fremfor Strategy Pattern — og når ville du byttet?
3. Hvorfor ligger valideringen i konstruktøren og ikke i `PricingService`?
4. Hvor kan avrunding ligge (konstruksjon, operasjon, sluttresultat), og hvorfor er tidlig avrunding av prosentfaktoren risikabelt?

## Kommandoer

```bash
./mvnw test -pl case-01-pure-kotlin-domain
```
