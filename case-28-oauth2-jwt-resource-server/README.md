# Case 28 - OAuth2/JWT resource server

## Domene
Sikkerhet / API-tilgang

## Tid
60-75 minutter

## Hva dette trener
- Spring Security / `SecurityFilterChain`
- OAuth2 resource server og JWT
- Scopes vs. roller, claim-mapping
- MockMvc security-tester (`jwt()`)

## Scenario
`ReportController` eksponerer tre endepunkter:
- `GET /public/ping` — skal være åpent uten token.
- `GET /api/reports` — skal kreve gyldig JWT med scope `reports:read`.
- `DELETE /api/reports/{id}` — skal kreve rollen `ADMIN` (fra et custom claim `roles` i tokenet).

`SecurityConfig` er ufullstendig: i dag slipper alt gjennom. Testene bruker MockMvc med `jwt()`-post-processor (ingen ekte identity provider nødvendig) og viser hva som forventes — flere av dem feiler til konfigurasjonen er på plass.

## Oppgave
Sikre API-et som OAuth2 resource server med granulære regler for scopes og roller — og bevis det med security-tester.

## TODO / fokusområder
- TODO: Fullfør `SecurityFilterChain`: åpne `/public/**`, krev autentisering på `/api/**`, og skru på JWT-basert resource server.
- TODO: Krev `SCOPE_reports:read` for `GET /api/reports` (velg: URL-regel i filterkjeden eller `@PreAuthorize` — og vær klar til å begrunne valget).
- TODO: Skriv en custom `JwtAuthenticationConverter` som mapper claimet `roles` til `ROLE_`-authorities, slik at `hasRole("ADMIN")` fungerer for delete-endepunktet.
- TODO: Avklar hvorfor CSRF er skrudd av for et stateless token-API, og hva `SessionCreationPolicy.STATELESS` gjør.
- TODO: Diskuter Zero Trust-prinsipper: eksplisitt autentisering per kall, minst mulig tilgang via scopes, og logging av tjenestetilgang. Hvor hører det hjemme i koden?

## Akseptansekriterier
- `/public/ping` gir 200 uten token.
- `/api/reports` gir 401 uten token, 403 med token uten riktig scope, 200 med `reports:read`.
- `DELETE /api/reports/{id}` gir 403 uten ADMIN-rolle og 204 med.
- Sikkerhetsreglene er dekket av MockMvc-tester — ingen manuell testing nødvendig.

## Formål i intervjuet
Sikkerhet er et tema intervjuere gjerne drar i når kandidaten nevner OAuth2/JWT/Maskinporten på CV-en: "Vis oss hvordan du ville sikret dette API-et." Da holder det ikke å forklare konseptene; du bør kunne skrive `SecurityFilterChain`-konfigurasjonen og en sikkerhetstest live. Ingen av case 1–25 dekker faktisk Spring Security-konfigurasjon (case-09 og case-17 abstraherer bevisst bort den) — dette caset lukker hullet.

## Ikke gjør det for lett
Ikke nøy deg med `anyRequest().authenticated()`. Poenget er granulære regler: scopes, roller og claim-mapping — og at alt er testet.

## Intervjuspørsmål / debrief
1. Forskjellen på autentisering og autorisasjon, og på scope vs. rolle?
2. Hvordan validerer resource serveren JWT-signaturen i produksjon (issuer-uri/JWKS)?
3. Hvorfor bør autorisasjonsregler ligge så nær domenet som mulig (jf. case-09), og hva bør ligge i filterkjeden?
4. Hvordan ville du satt opp dette mot Maskinporten i praksis (scopes, audience, token-utveksling)?

## Kommandoer

```bash
mvn test -pl case-28-oauth2-jwt-resource-server
```
