# Case 27 - Coroutines og structured concurrency

Parallelliser I/O-kall med `async`/`coroutineScope`, håndter timeout og feil — og test det med virtuell tid.

## Hvorfor dette caset er viktig for deg
Coroutines er det største Kotlin-temaet som IKKE dekkes av case 1–25, og det er et klassisk
intervjutema for senior Kotlin-utviklere: "Hvordan henter du data fra to tjenester samtidig?
Hva skjer hvis den ene feiler? Hva er structured concurrency?" Med din bakgrunn fra
integrasjonstunge systemer (SPK/NAV, Candidate Match som kaller LLM-API-er) er det naturlig at
Sopra Steria tester om du kan uttrykke samtidighet idiomatisk i Kotlin i stedet for
tråder/CompletableFuture fra Java-verdenen. Kan du forklare forskjellen på `launch` og `async`,
og hvorfor `GlobalScope` er en code smell, står du støtt.

## Scenario
`ConsultantSummaryService` bygger et sammendrag av en konsulent ved å hente profil og CV fra to
trege tjenester. Dagens implementasjon er sekvensiell og har ingen timeout- eller feilhåndtering.
Testene bruker `kotlinx-coroutines-test` med virtuell tid — én av dem feiler med vilje til du har
parallellisert kallene.

## TODO / fokusområder
- TODO: Parallelliser hentingen av profil og CV med `coroutineScope { async { ... } }`. Testen
  `fetches profile and cv in parallel` skal bli grønn (virtuell tid < 2000 ms).
- TODO: Legg på `withTimeout`/`withTimeoutOrNull` rundt CV-kallet og bestem hva sammendraget skal
  inneholde når CV-tjenesten er for treg (feile helt, eller degradere med "CV utilgjengelig"?).
- TODO: Avklar feilsemantikk: hvis profilkallet kaster, skal CV-kallet kanselleres? (Hint: det er
  akkurat dette structured concurrency gir deg gratis — vær klar til å forklare hvordan.)
- TODO: Skriv en test som verifiserer kansellering: når det ene kallet feiler, skal det andre
  ikke fullføre.
- TODO: Diskuter hvorfor `suspend`-funksjoner ikke bør blokkere tråden (`Thread.sleep` vs `delay`),
  og hvor `Dispatchers.IO` hører hjemme.

## Akseptansekriterier
- Profil og CV hentes parallelt; total virtuell tid = det tregeste kallet, ikke summen.
- Ingen `GlobalScope`, ingen `runBlocking` i produksjonskode.
- Feil i ett kall gir veldefinert oppførsel (kansellering eller degradering) — dokumentert i en test.
- Testene bruker virtuell tid (`runTest`) og kjører på millisekunder.

## Debrief-spørsmål du bør kunne svare på
- Hva er structured concurrency, og hva går galt uten det?
- `launch` vs `async` — når bruker du hva?
- Hva gjør `SupervisorJob`, og når trenger du det?
- Hvordan samspiller coroutines med Spring WebFlux/MVC (suspend controller-funksjoner)?

## Hvordan kjøre
```bash
mvn test -pl case-27-coroutines-structured-concurrency
```
