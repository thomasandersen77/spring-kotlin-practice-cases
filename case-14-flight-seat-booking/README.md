# Case 14 - Flight seat booking

## Domene
Flybooking

## Tid
60-75 minutter

## Hva dette trener
- Aggregate Root
- Consistency boundary
- Application service
- Domain event

## Scenario
Et fly skal kunne reservere og kansellere seter. Skeleton-koden returnerer et `SeatReserved`-event, men mangler state, duplicate-regler og kansellering.

## Oppgave
Modeller `Flight` som eier setereservasjonene sine. Implementer reservasjon og kansellering slik at samme sete ikke kan dobbelbookes og domain eventet representerer en faktisk tilstandsendring.

## TODO / fokusområder
- TODO: Legg intern state til `Flight` eller velg en eksplisitt alternativ modell og begrunn den.
- TODO: Hindre reservasjon av samme sete to ganger.
- TODO: Avklar om samme passasjer kan ha flere seter.
- TODO: Implementer kansellering for eksisterende reservasjon og bestem hva som skjer for ukjent sete.
- TODO: Legg til tester for dobbelbooking, kansellering og rebooking etter kansellering.

## Akseptansekriterier
- Reservasjon endrer faktisk flyets tilstand.
- Ugyldige overganger håndteres tydelig.
- Eventet er konsekvens av en godkjent handling, ikke bare et returnert DTO.
- Aggregatgrensen kan forklares.

## Formål i treningen
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste treningsvalg.

## Ikke gjør det for lett
Ikke returner `SeatReserved` uten å lagre noe. Da viser ikke caset konsistensgrensen som oppgaven trener på.

## Treningsspørsmål / debrief
1. Hva er konsistensgrensen til `Flight`-aggregatet?
2. Hvorfor er `SeatReserved` et domain event og ikke bare en DTO?
3. Hvor fanges dobbelbooking — domene, service eller database?

## Kommandoer

```bash
./mvnw test -pl case-14-flight-seat-booking
```
