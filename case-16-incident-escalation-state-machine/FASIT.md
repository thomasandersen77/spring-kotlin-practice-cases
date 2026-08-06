# Fasit – incident escalation state machine

## Domene og kontrakt

`Incident` eier gjeldende status og append-only overgangshistorikk. Hver kommando returnerer en `IncidentTransition` med korrekt fra-/til-status, actor og tidspunkt.

## Gyldige overganger og invariants

- `OPEN -> ACKNOWLEDGED -> IN_PROGRESS -> RESOLVED -> CLOSED`.
- `RESOLVED -> OPEN` er eneste reopen-overgang.
- Actor må være ikke-blank, og timestamps må være strengt stigende.
- State oppdateres og historikken lagres bare etter at alle guards er godkjent.
- Historikk eksponeres som snapshot.

En overgangstabell kunne skalert bedre dersom mange kommandoer delte samme semantikk. Navngitte metoder og én privat overgangsfunksjon gir her tydelig domenespråk uten framework. Closed incidents kan ikke reopenes fordi README eksplisitt beskriver reopen fra resolved.

## Edge cases og tester

Testene dekker state/event ved acknowledge, full livssyklus, reopen og ny syklus, ulovlig close, blank actor og ikke-stigende timestamp.

## Kort intervjuforklaring

Den private overgangsfunksjonen gjør oppdateringen atomisk fra aggregatets perspektiv: forventet status og auditdata valideres før status/historikk endres. Transition-resultatet gjør hendelsen observerbar og kan senere oversettes til audit eller domain event.
