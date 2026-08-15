# Case 08 - Refaktorer en feit controller

## Domene
Abonnement

## Tid
60 minutter

## Hva dette trener
- SRP / ansvarsseparasjon
- Refaktorering
- DTO-er og commands
- Primitive obsession
- Testbarhet

## Scenario
Koden er delvis ryddet, men den er fortsatt ikke ferdig refaktorert. Controlleren er tynnere, mens service-laget håndterer parsing, prisregler, persistence-detaljer og response-shape på én gang.

## Oppgave
Gjør designet mer uttrykksfullt uten å overdesigne. Du skal kunne forklare hva som er domenemodell, hva som er application service, og hvor DTO-er/repository-grenser bør ligge.

## TODO / fokusområder
- TODO: Erstatt utydelige `Map<String, String>`-request/response med små DTO-er eller commands der det gir mening.
- TODO: Flytt plan- og prisregler til et domenebegrep som er lett å teste for `BASIC`, `PRO` og `ENTERPRISE`.
- TODO: Avklar hva som skal skje ved ukjent plan, manglende customerId og ugyldig UUID.
- TODO: Bestem om kansellering av et allerede inaktivt abonnement skal være idempotent eller feile eksplisitt, og skriv test først.
- TODO: Vurder en repository-port hvis du vil isolere Spring Data fra use case-laget.

## Akseptansekriterier
- Prisregler kan testes uten Spring Boot-kontekst.
- Controlleren inneholder ikke forretningslogikk.
- Feiltilfeller er bevisst modellert, ikke bare tilfeldige exceptions fra parsing.
- Refaktoreringen er liten nok til å forklare i trening, men tydelig nok til å vise retning.

## Formål i treningen
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste treningsvalg.

## Ikke gjør det for lett
Ikke bare flytt eksisterende kode én fil ned. Målet er å vise bedre grenser og bedre språk i koden, samtidig som løsningen fortsatt er enkel.

## Treningsspørsmål / debrief
1. Hvilke ansvarsområder fant du i den feite controlleren, og hvor flyttet du dem?
2. Hvordan tester du prisreglene uten Spring?
3. Hva er igjen i controlleren etter refaktorering — og hvorfor akkurat det?

## Kommandoer

```bash
./mvnw test -pl case-08-refactor-fat-controller
```
