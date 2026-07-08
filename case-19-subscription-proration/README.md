# Case 19 - Subscription proration

## Domene
SaaS-abonnement

## Tid
60 minutter

## Hva dette trener
- Date/time
- Money
- Refactoring
- Perioderegler

## Scenario
Når en kunde oppgraderer abonnement midt i en fakturaperiode, skal kunden belastes for differansen i gjenværende periode. Koden gir kun modellene og en uimplementert calculator.

## Oppgave
Implementer proratering på en måte som tydelig håndterer inkluderende datoer, avrunding og ugyldige perioder.

## TODO / fokusområder
- TODO: Avklar om `changeDate` er første dag på ny plan, og hvordan `endInclusive` påvirker antall dager.
- TODO: Beregn differanse mellom nåværende og ny plan bare for gjenstående del av perioden.
- TODO: Håndter nedgradering, lik pris og `changeDate` utenfor perioden bevisst.
- TODO: Velg avrunding/skala for pengebeløp og legg test rundt en ikke-heltallig dagssats.
- TODO: Valider at `BillingPeriod` har gyldig start/slutt.

## Akseptansekriterier
- Midt-i-perioden-oppgradering har en etterprøvbar forventet verdi i test.
- Første/siste dag i perioden er testet.
- Negative eller null charges er bevisst håndtert.
- Dato- og money-regler er lesbare uten å kjenne hele SaaS-domenet.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke bare returner et positivt tall. Proration-caset handler om inkluderende datoer og presisjon.

## Kommandoer

```bash
mvn -pl case-19-subscription-proration test
```
