# Case 46 – Spring Security-testing med MockMvc

## Domene
Meldings-API med separate lese- og skriverettigheter.

## Tid
45–60 minutter.

## Vanskelighetsgrad
Lav til medium.

## Hva dette trener
- MockMvc med Spring Security
- uautentisert kontra uautorisert bruker
- mock JWT og authorities
- 200, 201, 401 og 403
- controller-/serviceisolasjon
- valg av riktig test-slice

## Scenario
Produksjonsfixturet er lite og ferdig nok til å være testmål. Oppgaven er å skrive presise security-tester som beviser API-kontrakten uten å teste hele Spring Security-rammeverket.

## Oppgave
Fullfør de fire tomme testene i `MessageSecurityTest`. Bruk `spring-security-test`, MockMvc og mock JWT. Verifiser bare domenerelevant delegering til servicen.

## Gjeldende kontrakt
- Manglende token gir 401.
- `SCOPE_messages:read` gir tilgang til GET.
- Samme read-scope gir 403 på POST.
- `SCOPE_messages:write` gir 201 på POST.
- Controlleren delegerer til service ved tillatt request.
- Testen skal ikke starte database eller full applikasjon.

## TODO / fokusområder
1. Implementer 401-test uten autentisering.
2. Lag mock JWT med read-authority og test GET.
3. Bevis 403 for autentisert bruker med feil authority.
4. Stub service og test POST med write-authority.
5. Verifiser nødvendig servicekall, men ikke framework-interaksjoner.
6. Begrunn `@WebMvcTest` fremfor `@SpringBootTest`.

## Forslag til arbeidsrekkefølge
1. Få 401-testen grønn.
2. Legg på én authority og se 200.
3. Gjenbruk tokenet mot feil operasjon og se 403.
4. Test request/response og serviceisolasjon til slutt.

## Akseptansekriterier
- Alle fire testene er grønne.
- 401 og 403 skilles eksplisitt.
- Mock JWT brukes uten ekte token eller IdP.
- Service er en mock i web-slicen.
- Testene verifiserer status, nødvendig JSON og relevant delegasjon.
- Ingen full Spring Boot-kontekst brukes.

## Ikke gjør det for lett
Ikke deaktiver filterkjeden, bruk `@WithMockUser` når oppgaven spesifikt trener JWT, eller kopier frameworkets egne tester for hver konfigurasjonslinje.

## Formål i treningen
Caset trener en svært praktisk ferdighet: å bevise sikkerhetskontrakten raskt og presist uten tung ende-til-ende-rigg.

## Treningsspørsmål / debrief
1. Hva er forskjellen på 401 og 403 i disse testene?
2. Hva gjør MockMvc request postprocessor `jwt()`?
3. Hva blir ikke testet når JWT-en mockes?
4. Hvorfor er `@WebMvcTest` et godt valg her?
5. Når trenger du en full resource-server-integrasjonstest?
6. Hvilke serviceinteraksjoner er domenerelevante å verifisere?

## Kommandoer
```bash
./mvnw test -pl case-46-spring-security-mockmvc
./mvnw verify -pl case-46-spring-security-mockmvc
```
