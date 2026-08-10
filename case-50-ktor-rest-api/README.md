# Case 50 – Ktor REST API

## Domene
Lite asynkront oppgave-API.

## Tid
45–60 minutter.

## Vanskelighetsgrad
Medium.

## Hva dette trener
- Ktor routing
- request-/response-serialisering
- application-/service-lag
- eksplisitt dependency setup
- `testApplication`
- suspending servicekall og naturlig coroutine-flyt
- Kotlin-native web-API uten Spring

## Scenario
En suspending `TaskService` skal eksponeres som GET og POST. Caset holder domenet lite slik at fokuset er Ktor-pipeline, routing og testoppsett.

## Oppgave
Implementer GET- og POST-routene i `taskApi`, valider input og returner korrekte statuskoder og JSON.

## Gjeldende kontrakt
- `GET /tasks/{id}` gir 200 eller 404.
- Ugyldig id gir 400.
- `POST /tasks` med gyldig tittel gir 201.
- Blank tittel gir 400.
- JSON håndteres av Content Negotiation/Jackson.
- Servicekall er suspending uten blocking-broer.

## TODO / fokusområder
1. Parse og valider path-parameteren.
2. Kall suspending service og map null til 404.
3. Deserialiser `CreateTaskRequest`.
4. Valider blank tittel og map til 400.
5. Returner 201 med response-DTO.
6. Utvid `testApplication` med JSON- og feilassertions.
7. Vurder hvordan dependencies wires i en større Ktor-app.

## Forslag til arbeidsrekkefølge
1. Få GET happy path grønn.
2. Legg til 404 og ugyldig id.
3. Implementer POST og serialisering.
4. Avslutt med valideringsgrensen og refaktorering av routing.

## Akseptansekriterier
- GET og POST følger statuskodekontrakten.
- Request og response serialiseres uten manuell JSON-bygging.
- Service er eksplisitt injisert i application-modulen.
- Ingen `runBlocking` brukes i route handlers.
- Testene bruker Ktors `testApplication`.
- Feilflyt og happy path er dekket.

## Ikke gjør det for lett
Ikke hardkod responses i testfixturet, bruk global service-locator eller blokker coroutine-tråden for å kalle suspending kode.

## Formål i intervjuet
Caset gir praktisk erfaring med en Kotlin-native serverstack og gjør det lettere å diskutere coroutines, plugins og eksplisitt wiring mot Spring.

## Intervjuspørsmål / debrief
1. Hvorfor er route handlers naturlig suspending i Ktor?
2. Hva tilsvarer Spring-konfigurasjon i en Ktor application module?
3. Hvordan fungerer Content Negotiation?
4. Hvordan injecter du dependencies uten å skjule dem globalt?
5. Hva tester `testApplication`, og hva trenger en senere integrasjonstest?
6. Når er Ktor et bedre eller dårligere valg enn Spring Boot?

## Kommandoer
```bash
./mvnw test -pl case-50-ktor-rest-api
./mvnw verify -pl case-50-ktor-rest-api
```
