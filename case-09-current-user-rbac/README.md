# Case 09 - CurrentUser og tilgangskontroll

## Domene
Saksbehandling / tilgang

## Tid
60 minutter

## Hva dette trener
- RBAC / access policy
- Testbarhet uten security-rammeverk
- Tynn controller
- Eksplisitt feilmodellering

## Scenario
En saksbehandler skal kunne lukke saker, men tilgangen avhenger av rolle, organisasjon og sakens status. Koden har en forenklet `@CurrentUser`-annotation og en `AccessPolicy`, men use case-flyten er ikke ferdig.

## Oppgave
Gjør lukking av sak til et tydelig use case. Access policy skal kunne testes uten `SecurityContextHolder`, mens controlleren bare oversetter HTTP/request-data til application service-kall.

## TODO / fokusområder
- TODO: Rydd opp i API-kontrakten: skal `caseId` komme fra path, request body eller begge deler?
- TODO: Bruk `AccessPolicy` i service-laget sammen med en sak hentet fra repository eller en enkel testbar port.
- TODO: Modellér manglende tilgang eksplisitt, for eksempel med en egen exception eller et resultatobjekt.
- TODO: Test `ADMIN`, `READ_ONLY`, `CASE_WORKER` i samme organisasjon og `CASE_WORKER` i feil organisasjon.
- TODO: Diskuter hvordan en ekte JWT/Spring Security-integrasjon kunne fylt `User`, uten at domenet avhenger av security-rammeverket.

## Akseptansekriterier
- `AccessPolicy` har raske enhetstester uten Spring.
- Controlleren er tynn og inneholder ikke RBAC-regler.
- Service-laget viser hvor current user, sak og policy møtes.
- Uautoriserte flyter er like bevisst håndtert som happy path.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.

## Ikke gjør det for lett
Ikke gjør alle med riktig rolle automatisk autorisert. Caset handler om kombinasjonen rolle, organisasjon og sakstilstand.

## Intervjuspørsmål / debrief
1. Hvorfor skal `AccessPolicy` kunne testes uten `SecurityContextHolder`?
2. Hvor hører autorisasjonsreglene hjemme — controller, service eller domene?
3. Hvordan ville en ekte JWT fylt `User` uten å lekke inn i domenet?

## Kommandoer

```bash
./mvnw test -pl case-09-current-user-rbac
```
