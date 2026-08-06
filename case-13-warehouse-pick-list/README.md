# Case 13 - Warehouse pick list

## Domene
Lager / ordreplukk

## Tid
60 minutter

## Hva dette trener
- Aggregate Root
- State transitions
- Invariants
- Modellering av intern state

## Scenario
En plukkliste skal bygges opp av SKU-linjer, markeres plukket og fullføres når alle linjer er håndtert. Skeleton-koden har metoder, men ingen state eller regler ennå.

## Oppgave
Design `PickList` som et lite aggregate med interne linjer og status. Vis hvordan du beskytter invariants når linjer legges til, plukkes og fullføres.

## TODO / fokusområder
- TODO: Valider `Quantity` og avklar om samme SKU skal merges eller avvises.
- TODO: Hindre endringer etter at plukklisten er fullført.
- TODO: Implementer `markPicked` for eksisterende SKU og bestem hva som skjer for ukjent SKU.
- TODO: Tillat `complete` bare når listen har linjer og alle er plukket.
- TODO: Legg til tester for ugyldig quantity, ukjent SKU og fullføring for tidlig.

## Akseptansekriterier
- `PickList` har nok state til å håndheve egne regler.
- Statusoverganger er eksplisitte og testet.
- Feiltilfeller er bevisst modellert.
- Koden kan forklares som et aggregate, ikke bare som en datastruktur.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.

## Ikke gjør det for lett
Ikke la metodene være tomme “commands” uten observerbar state. Kandidaten må kunne vise hvordan invariants faktisk holdes.

## Intervjuspørsmål / debrief
1. Hvilken state må `PickList` eie for å håndheve reglene sine?
2. Hvordan modellerer du ugyldige overganger — exception eller resultatobjekt?
3. Hvorfor er spørsmålet om samme SKU skal merges eller avvises et domenevalg?

## Kommandoer

```bash
mvn test -pl case-13-warehouse-pick-list
```
