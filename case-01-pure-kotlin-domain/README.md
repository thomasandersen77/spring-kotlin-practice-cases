# Case 01 - Ren Kotlin domenemodell

Øv på domenemodellering i ren Kotlin uten Spring-støy: value classes, sealed classes, null-safety, money-beregning og tester.

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

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke bare få nåværende tester grønne. Legg til minst én test som tvinger deg til å ta et designvalg rundt validering eller avrunding.

## Hvordan kjøre

```bash
mvn test
mvn spring-boot:run
```
