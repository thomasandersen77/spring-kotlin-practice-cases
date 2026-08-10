# Case 49 – http4k functional HTTP

## Domene
Lite tilbuds-API for produkt og antall.

## Tid
45–60 minutter.

## Vanskelighetsgrad
Medium.

## Hva dette trener
- http4k routing
- `HttpHandler` som funksjon
- request-/response-lenses
- `Filter` som middleware
- eksplisitt dependency composition
- testing uten Spring-kontekst
- Kotlin-funksjoner som arkitekturbyggesteiner

## Scenario
En prisservice skal eksponeres som `POST /quotes`. Teamet vil se hvordan samme type HTTP-use-case ser ut når routing og middleware komponeres eksplisitt som funksjoner.

## Oppgave
Implementer lenses, routing og request-id-filter. `QuoteService` er en injisert funksjonell avhengighet og skal ikke gjemmes i global tilstand.

## Gjeldende kontrakt
- `POST /quotes` med gyldig JSON gir 200 og tilbuds-JSON.
- Blank `productCode` eller `quantity <= 0` gir 400.
- Ukjent route gir 404.
- `X-Request-ID` gjenbrukes dersom den finnes, ellers genereres den.
- Filteret legger request-id på både suksess- og feilresponser.
- Handleren kan testes som en vanlig funksjon.

## TODO / fokusområder
1. Definer body-lenses med Jackson.
2. Bind `POST /quotes` til en liten handler.
3. Valider input og map lens-/domeneutfall til HTTP.
4. Implementer request-id-filter.
5. Komponer filter, routes og service eksplisitt.
6. Legg til test av generert request-id og ukjent route.

## Forslag til arbeidsrekkefølge
1. Bygg en ren POST-handler og få happy path grønn.
2. Introduser lenses og valideringsfeil.
3. Komponer routing.
4. Legg request-id-filteret ytterst og test alle responser.

## Akseptansekriterier
- Appen er en `HttpHandler` som kan kalles direkte i test.
- Lenses brukes der de forbedrer typesikkerhet.
- Filteret er uavhengig av domeneservicen.
- Ingen Spring-kontekst eller DI-container introduseres.
- Request-id følger alle responser.
- Testsuiten dekker happy path, 400 og 404.

## Ikke gjør det for lett
Ikke parse JSON manuelt med string-operasjoner, legg dependencies i globale objekter eller skriv en stor `when` som erstatter routing og filters.

## Formål i intervjuet
Caset viser et Kotlin-native, funksjonelt alternativ og gir konkrete kontraster til annotations- og containerdrevet Spring-kode.

## Intervjuspørsmål / debrief
1. Hva betyr det konkret at en `HttpHandler` er en funksjon?
2. Hva gir lenses sammenlignet med manuell parsing?
3. Hvordan tilsvarer `Filter` Spring-filter/interceptor?
4. Hvor skjer dependency injection uten container?
5. Hvorfor er testen rask uten Spring context?
6. Når er eksplisitt komposisjon en fordel, og når blir wiring støy?

## Kommandoer
```bash
./mvnw test -pl case-49-http4k-functional-http
./mvnw verify -pl case-49-http4k-functional-http
```
