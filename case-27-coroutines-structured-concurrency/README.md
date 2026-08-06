# Case 27 - Coroutines og structured concurrency

## Domene
Integrasjon / samtidighet

## Tid
45-60 minutter

## Hva dette trener
- Coroutines og `suspend`-funksjoner
- Structured concurrency (`coroutineScope`, `async`)
- Timeout og kansellering
- Testing med virtuell tid (`runTest`)

## Scenario
`ConsultantSummaryService` bygger et sammendrag av en konsulent ved å hente profil og CV fra to trege tjenester. Dagens implementasjon er sekvensiell og har ingen timeout- eller feilhåndtering. Testene bruker `kotlinx-coroutines-test` med virtuell tid — én av dem feiler med vilje til du har parallellisert kallene.

## Oppgave
Parallelliser I/O-kallene med `coroutineScope { async { ... } }`, legg på timeout og avklar feil-/kanselleringssemantikk — og test det med virtuell tid.

## TODO / fokusområder
- TODO: Parallelliser hentingen av profil og CV med `coroutineScope { async { ... } }`. Testen `fetches profile and cv in parallel` skal bli grønn (virtuell tid < 2000 ms).
- TODO: Legg på `withTimeout`/`withTimeoutOrNull` rundt CV-kallet og bestem hva sammendraget skal inneholde når CV-tjenesten er for treg (feile helt, eller degradere med "CV utilgjengelig"?).
- TODO: Avklar feilsemantikk: hvis profilkallet kaster, skal CV-kallet kanselleres? (Hint: det er akkurat dette structured concurrency gir deg gratis — vær klar til å forklare hvordan.)
- TODO: Skriv en test som verifiserer kansellering: når det ene kallet feiler, skal det andre ikke fullføre.
- TODO: Diskuter hvorfor `suspend`-funksjoner ikke bør blokkere tråden (`Thread.sleep` vs `delay`), og hvor `Dispatchers.IO` hører hjemme.

## Akseptansekriterier
- Profil og CV hentes parallelt; total virtuell tid = det tregeste kallet, ikke summen.
- Ingen `GlobalScope`, ingen `runBlocking` i produksjonskode.
- Feil i ett kall gir veldefinert oppførsel (kansellering eller degradering) — dokumentert i en test.
- Testene bruker virtuell tid (`runTest`) og kjører på millisekunder.

## Formål i intervjuet
Coroutines er et klassisk intervjutema for senior Kotlin-utviklere: "Hvordan henter du data fra to tjenester samtidig? Hva skjer hvis den ene feiler? Hva er structured concurrency?" Kan du forklare forskjellen på `launch` og `async`, og hvorfor `GlobalScope` er en code smell, står du støtt — spesielt med bakgrunn fra integrasjonstunge systemer.

## Ikke gjør det for lett
Ikke bruk `GlobalScope` eller `runBlocking` for å "få det til å virke". Poenget er structured concurrency — ikke bare parallellisering.

## Intervjuspørsmål / debrief
1. Hva er structured concurrency, og hva går galt uten det?
2. `launch` vs `async` — når bruker du hva?
3. Hva gjør `SupervisorJob`, og når trenger du det?
4. Hvordan samspiller coroutines med Spring WebFlux/MVC (suspend controller-funksjoner)?

## Kommandoer

```bash
./mvnw test -pl case-27-coroutines-structured-concurrency
```
