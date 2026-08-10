# Case 51 – Kotlin web framework comparison

## Domene
Teknologivalg for et lite internt API.

## Tid
30–45 minutter.

## Vanskelighetsgrad
Medium, med seniorfokus på begrunnelse.

## Hva dette trener
- sammenligning av Spring Boot, http4k og Ktor
- framework magic kontra eksplisitt komposisjon
- dependency injection
- testbarhet
- coroutines
- middleware/filter/plugins
- pragmatiske trade-offs og Kotlin-idiomatikk

## Scenario
Et team skal velge stack for et lite API. Behovene varierer mellom moden Spring Security/JPA-integrasjon, funksjonell komposisjon og coroutine-first I/O. Oppgaven er ikke å implementere samme API tre ganger, men å gi en forsvarlig anbefaling.

## Oppgave
Fullfør sammenligningsmatrisen og `recommend`. Hver anbefaling skal inneholde positive grunner og en konkret begrunnelse for hvorfor alternativene ikke ble valgt i akkurat dette scenariet.

## Gjeldende kontrakt
- Alle tre rammeverk beskrives én gang.
- Spring Boot kan anbefales ved JPA, method security og eksisterende teamkompetanse.
- http4k kan anbefales ved eksplisitt funksjonell komposisjon.
- Ktor kan anbefales ved coroutine-first serverbehov.
- Anbefalingen må beskrive trade-offs, ikke bare rangere popularitet.
- Samme input skal gi deterministisk anbefaling.

## TODO / fokusområder
1. Beskriv DI-/komposisjonsmodellen for hver stack.
2. Sammenlign filter/interceptor, http4k Filter og Ktor plugins.
3. Beskriv coroutine-støtte presist.
4. Implementer en enkel, lesbar beslutningsregel.
5. Begrunn avviste alternativer konkret.
6. Forbered en muntlig anbefaling på maksimalt to minutter.
7. Beskriv hva en spike måtte bevise før et produksjonsvalg.

## Forslag til arbeidsrekkefølge
1. Fyll matrisen med korte, faktiske egenskaper.
2. Prioriter kravene eksplisitt.
3. Implementer de tre representative anbefalingsscenariene.
4. Debrief høyt uten å lese fra matrisen.

## Akseptansekriterier
- Profilene dekker alle tre rammeverk uten markedsføringsspråk.
- Testscenariene gir begrunnede og deterministiske anbefalinger.
- Ingen stack fremstilles som universelt best.
- Anbefalingen knyttes direkte til oppgitte krav.
- Det bygges ikke tre komplette webapplikasjoner.
- Debrief dekker både teknikk, team og operasjonelle trade-offs.

## Ikke gjør det for lett
Ikke velg Spring fordi «alle bruker det», Ktor fordi «det er Kotlin» eller http4k fordi «funksjonelt er renere». Ikke lag tre komplette adaptere; dette er et beslutnings- og debrief-case.

## Formål i intervjuet
Caset trener seniorferdigheten å velge et tilstrekkelig verktøy for konteksten og forklare valget presist uten rammeverkslojalitet.

## Intervjuspørsmål / debrief
1. Når er Spring Boot det mest pragmatiske valget?
2. Hva vinner og taper du med http4ks eksplisitte funksjonskomposisjon?
3. Når betyr Ktors coroutine-first-modell noe i praksis?
4. Hvordan påvirker teamkompetanse et teknisk valg?
5. Hvordan sammenligner du testbarhet uten å telle bare testoppstartstid?
6. Hvilke observability- og driftskrav ville påvirket valget?
7. Hva må en kort spike bevise før beslutningen tas?

## Kommandoer
```bash
./mvnw test -pl case-51-kotlin-web-framework-comparison
./mvnw verify -pl case-51-kotlin-web-framework-comparison
```
