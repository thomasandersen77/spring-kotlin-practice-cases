# Case 17 - Incident escalation state machine

## Domene
Drift / incidents

## Tid
60 minutter

## Hva dette trener
- State transitions
- Domain behavior
- Clean code
- Historikk/audit

## Scenario
Et incident går gjennom statuser fra `OPEN` til `CLOSED`, med mulighet for gjenåpning fra løst til åpent. Skeleton-koden har overgangsmetoder, men ingen state eller validering.

## Oppgave
Implementer en liten state machine som bare tillater gyldige overganger og returnerer overgangsinformasjon med actor og timestamp.

## TODO / fokusområder
- TODO: La `Incident` holde nåværende status, og vurder om overgangshistorikk også skal lagres.
- TODO: Implementer gyldige overganger: acknowledge, startWork, resolve, close og reopen.
- TODO: Hindre ulovlige overganger, som close direkte fra `OPEN`.
- TODO: Valider actor/timestamp nok til at historikken blir meningsfull.
- TODO: Legg til tester som følger en hel livssyklus og minst én ugyldig overgang.

## Akseptansekriterier
- Nåværende status oppdateres konsistent etter hver handling.
- Ugyldige overganger feiler eksplisitt.
- `IncidentTransition` beskriver riktig `from`, `to`, actor og tidspunkt.
- Koden viser state machine-logikk uten å bli et stort framework.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke returner overgangsobjekter uten å endre status. Da testes ikke state machine-delen av caset.

## Kommandoer

```bash
mvn -pl case-17-incident-escalation-state-machine test
```
