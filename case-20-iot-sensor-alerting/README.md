# Case 20 - IoT sensor alerting

## Domene
IoT / sensordata

## Tid
60 minutter

## Hva dette trener
- Ports/adapters
- Strategy Pattern
- Test doubles
- Alert-regler

## Scenario
Sensoravlesninger skal prosesseres og publisere alert når verdier bryter terskler. `AlertPublisher` er allerede en port, men terskelstrategi og alert-regler mangler.

## Oppgave
Implementer use case-et slik at terskler per sensortype er isolert fra publisering. Bruk test doubles til å vise at alerts publiseres når de skal, og ikke ellers.

## TODO / fokusområder
- TODO: Definer terskelregler for `TEMPERATURE`, `HUMIDITY` og `VIBRATION` uten å hardkode alt ustrukturert i use case-et.
- TODO: Publiser alert med nyttig melding når terskel brytes.
- TODO: Test at normal verdi ikke publiserer alert.
- TODO: Avklar hva som skjer for ukjent sensorId, NaN/Infinity eller negative verdier der det ikke gir mening.
- TODO: Vurder strategi-objekter eller en liten regel-tabell, og forklar tradeoff.

## Akseptansekriterier
- `AlertPublisher` kan byttes ut med test double uten ekstra rammeverk.
- Terskelreglene er samlet og testbare.
- Use case-et skiller beslutning om alert fra selve publiseringen.
- Både alert og no-alert-flyt er dekket av tester.

## Formål i treningen
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste treningsvalg.

## Ikke gjør det for lett
Ikke skriv én `if` som bare tilfredsstiller temperaturtesten. Caset skal vise utvidbar regelstruktur for flere sensortyper.

## Treningsspørsmål / debrief
1. Strategy-objekter vs. regeltabell — hva valgte du, og hvorfor?
2. Hvordan viser en test double at alert publiseres kun når den skal?
3. Hva er porten her, og hva kunne adapteren vært i produksjon?

## Kommandoer

```bash
./mvnw test -pl case-20-iot-sensor-alerting
```
