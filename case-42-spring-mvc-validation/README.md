# Case 42 – Spring MVC controller, service og validering

## Domene
Enkel oppgaveplanlegging for et utviklingsteam.

## Tid
45–60 minutter.

## Vanskelighetsgrad
Lav til medium.

## Hva dette trener
- tynn `@RestController`
- request-/response-DTO-er
- Bean Validation med Kotlin use-site targets
- application service og tydelig ansvarsdeling
- MockMvc og feilflyt
- statuskode og JSON-kontrakt

## Scenario
Et team trenger et lite endepunkt for å opprette arbeidsoppgaver med tittel og prioritet. Controlleren skal eie HTTP-detaljene, mens servicen skal eie opprettelsesflyten. Ugyldige requests skal avvises før servicen kalles.

## Oppgave
Fullfør serviceflyten og utvid testene slik at API-kontrakten dokumenteres. Hold controlleren tynn og returner aldri en intern modell direkte dersom du velger å innføre en slik.

## Gjeldende kontrakt
- `POST /api/tasks` med gyldig request gir 201.
- Blank tittel gir 400.
- Tittel kan være maksimalt 80 tegn.
- Utelatt prioritet blir `NORMAL`.
- Response inneholder id, normalisert tittel og prioritet.
- Ugyldig request skal ikke delegere til service.

## TODO / fokusområder
1. Implementer `TaskService.create` med en enkel, testbar opprettelsesflyt.
2. Vurder hvor trimming/normalisering skal skje.
3. Legg til test av maksimal tittellengde og default-prioritet.
4. Verifiser at ugyldig input ikke kaller servicen.
5. Vurder en stabil API-feilmodell uten å løse Case 48 på nytt.

## Forslag til arbeidsrekkefølge
1. Kjør testene og les den røde servicekontrakten.
2. Få happy path grønn.
3. Test valideringsgrenser gjennom MockMvc.
4. Refaktorer navn og mapping uten å flytte regler til controlleren.

## Akseptansekriterier
- Gyldig POST gir 201 og forventet JSON.
- Blank og for lang tittel gir 400.
- Controlleren inneholder ikke forretningslogikk.
- Bean Validation bruker `@field:` korrekt i Kotlin.
- Testene skiller controllerkontrakt fra servicelogikk.
- Modulen bruker ikke Spring-kontekst der en ren enhetstest er nok.

## Ikke gjør det for lett
Ikke returner konstante svar fra controlleren, legg all validering i `if`-blokker i web-laget eller gjør servicen til en tom pass-through bare for å få MockMvc-testene grønne.

## Formål i intervjuet
Caset demonstrerer den vanligste Spring-flyten i liten skala og lar deg forklare hvorfor validering, HTTP og applikasjonslogikk har ulike eiere.

## Intervjuspørsmål / debrief
1. Hvorfor bør controlleren være tynn?
2. Hva gjør `@field:` foran en Bean Validation-annotasjon i Kotlin?
3. Når tester du med MockMvc, og når holder en ren enhetstest?
4. Hvor bør normalisering av input ligge?
5. Hva er forskjellen på request-DTO og domenemodell?
6. Hvordan ville du gjort feilresponsen stabil for klienter?

## Kommandoer
```bash
./mvnw test -pl case-42-spring-mvc-validation
./mvnw verify -pl case-42-spring-mvc-validation
```
