# Case 25 - Idempotent command processing

## Domene
Betaling / robust kommandohåndtering

## Tid
60-75 minutter

## Hva dette trener
- Idempotency keys
- Retry-sikker applikasjonslogikk
- Ports/adapters mot ekstern gateway
- Feilklassifisering og resultatmodellering

## Scenario
Et API-endepunkt kan få samme betalingskommando flere ganger på grunn av nettverksfeil og retry. Systemet skal ikke dobbeltbelaste kunden når samme idempotency key kommer igjen.

## Oppgave
Implementer en use case som bruker idempotency key til å skille nye kall fra duplikater. Hold ekstern betalingsgateway bak en port, og gjør resultatene eksplisitte.

## TODO / fokusområder
- TODO: Normalisér og valider idempotency key tidlig.
- TODO: Returner tidligere resultat ved duplikat, uten nytt gateway-kall.
- TODO: Avklar hva som skjer hvis lagring av idempotency-resultat feiler etter vellykket charge.
- TODO: Modellér forskjell på forretningsfeil og teknisk feil.
- TODO: Skriv test som dokumenterer at samme key er idempotent selv med gjentatte kall.

## Akseptansekriterier
- Ny key gir prosessering via gateway.
- Eksisterende key gir `AlreadyProcessed` med tidligere receipt.
- Flowen er testbar med enkle test doubles.
- Koden viser tydelig grense mellom use case og gateway-port.

## Formål i treningen
Målet er å vise modenhet rundt robusthet: du skal kunne forklare hvordan systemet oppfører seg når nettverk, klienter eller retries ikke er perfekte, uten å bryte domenekonsistens.

## Ikke gjør det for lett
Ikke bruk bare “if exists return” uten å tenke på race conditions og lagringsrekkefølge. Caset handler om kontrollert atferd under feil og retry.

## Treningsspørsmål / debrief
1. Hva er en idempotency key, og hvem genererer den?
2. Hvorfor returnere tidligere resultat i stedet for å prosessere på nytt?
3. Hva skjer hvis lagring av idempotency-resultat feiler etter vellykket charge?

## Kommandoer

```bash
./mvnw test -pl case-25-idempotent-command-processing
```
