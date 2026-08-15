# Case 48 – ControllerAdvice og stabil API-feilmodell

## Domene
Ordre-API med ikke-funnet, konflikt og valideringsfeil.

## Tid
45–60 minutter.

## Vanskelighetsgrad
Lav til medium.

## Hva dette trener
- `@RestControllerAdvice`
- domene-/application exceptions
- stabil API-feil-DTO
- Bean Validation-feil per felt
- HTTP-statuskoder
- MockMvc-testing av feilflyt

## Scenario
Klientene trenger forutsigbare feilresponser og skal ikke kjenne Java/Kotlin-exceptionnavn eller Spring-intern struktur. Flere feiltyper må oversettes konsekvent ett sted.

## Oppgave
Implementer `OrderErrorHandler` for 404, 409 og 400. Feilmodellen skal være stabil, enkel og deterministisk.

## Gjeldende kontrakt
- `OrderNotFound` blir 404 og koden `ORDER_NOT_FOUND`.
- `OrderConflict` blir 409 og koden `ORDER_CONFLICT`.
- Bean Validation-feil blir 400 og koden `VALIDATION_ERROR`.
- Feltfeil inneholder feltnavn og relevant melding, sortert på feltnavn.
- Stacktrace, exceptionklasse og intern implementasjon skal ikke lekke.

## TODO / fokusområder
1. Map `OrderNotFound` til 404.
2. Map `OrderConflict` til 409.
3. Samle `MethodArgumentNotValidException` til feltfeil.
4. Sorter feltfeil deterministisk.
5. Vurder fallback for uventede feil uten å skjule observability.
6. Test status, content type og JSON-kontrakt med MockMvc.

## Forslag til arbeidsrekkefølge
1. Få 404-testen grønn.
2. Gjenbruk feilmodellen for 409.
3. Implementer validation mapping med to samtidige feltfeil.
4. Refaktorer felles response-bygging dersom det faktisk reduserer støy.

## Akseptansekriterier
- 404, 409 og 400 har korrekt semantikk.
- Feilresponsen følger én stabil DTO.
- Valideringsfeil er deterministiske og nyttige for klienten.
- Controlleren har ingen lokale `try/catch`-blokker.
- Ingen intern exceptiontype eksponeres i JSON.
- MockMvc-testene dokumenterer kontrakten.

## Ikke gjør det for lett
Ikke returner `Map<String, Any>` overalt, svar 500 på forventede domeneutfall eller legg `try/catch` i hver controller-metode.

## Formål i treningen
Caset trener oversettelsesgrensen mellom domene/applikasjon og HTTP, med konkrete statuskode- og kontraktvalg som er lette å diskutere.

## Treningsspørsmål / debrief
1. Hvorfor bør API-et ha en stabil feilmodell?
2. Hva skiller 400, 404, 409 og 500?
3. Hvorfor er `ControllerAdvice` bedre enn lokale `try/catch`?
4. Hvordan håndterer du flere valideringsfeil på samme felt?
5. Hva bør logges for en uventet feil uten å lekke det til klienten?
6. Når ville du brukt RFC 9457 Problem Details?

## Kommandoer
```bash
./mvnw test -pl case-48-controller-advice-errors
./mvnw verify -pl case-48-controller-advice-errors
```
