# Case 28 - OAuth2/JWT resource server

Sikre et REST-API med Spring Security som OAuth2 resource server: scopes, roller og testbar sikkerhetskonfigurasjon.

## Hvorfor dette caset er viktig for deg
CV-en din fremhever Maskinporten, OAuth2, JWT og Zero Trust fra SPK — som første
pensjonsleverandør i produksjon mot NAV. Det er nesten garantert at en intervjuer plukker opp
denne tråden: "Du har jobbet med Maskinporten — vis oss hvordan du ville sikret dette API-et."
Da holder det ikke å forklare konseptene; du bør kunne skrive `SecurityFilterChain`-konfigurasjonen
og en sikkerhetstest live. Ingen av case 1–25 dekker faktisk Spring Security-konfigurasjon
(case-09 og case-17 abstraherer bevisst bort Spring Security). Dette caset lukker det hullet, og
kobler det du gjorde hos SPK til konkret, moderne Spring Boot 3-kode.

## Scenario
`ReportController` eksponerer tre endepunkter:
- `GET /public/ping` — skal være åpent uten token.
- `GET /api/reports` — skal kreve gyldig JWT med scope `reports:read`.
- `DELETE /api/reports/{id}` — skal kreve rollen `ADMIN` (fra et custom claim `roles` i tokenet).

`SecurityConfig` er ufullstendig: i dag slipper alt gjennom. Testene bruker MockMvc med
`jwt()`-post-processor (ingen ekte identity provider nødvendig) og viser hva som forventes —
flere av dem feiler til konfigurasjonen er på plass.

## TODO / fokusområder
- TODO: Fullfør `SecurityFilterChain`: åpne `/public/**`, krev autentisering på `/api/**`, og
  skru på JWT-basert resource server.
- TODO: Krev `SCOPE_reports:read` for `GET /api/reports` (velg: URL-regel i filterkjeden eller
  `@PreAuthorize` — og vær klar til å begrunne valget).
- TODO: Skriv en custom `JwtAuthenticationConverter` som mapper claimet `roles` til
  `ROLE_`-authorities, slik at `hasRole("ADMIN")` fungerer for delete-endepunktet.
- TODO: Avklar hvorfor CSRF er skrudd av for et stateless token-API, og hva `SessionCreationPolicy.STATELESS` gjør.
- TODO: Diskuter Zero Trust-prinsippene fra SPK-prosjektet: eksplisitt autentisering per kall,
  minst mulig tilgang via scopes, og logging av tjenestetilgang. Hvor hører det hjemme i koden?

## Akseptansekriterier
- `/public/ping` gir 200 uten token.
- `/api/reports` gir 401 uten token, 403 med token uten riktig scope, 200 med `reports:read`.
- `DELETE /api/reports/{id}` gir 403 uten ADMIN-rolle og 204 med.
- Sikkerhetsreglene er dekket av MockMvc-tester — ingen manuell testing nødvendig.

## Debrief-spørsmål du bør kunne svare på
- Forskjellen på autentisering og autorisasjon, og på scope vs. rolle?
- Hvordan validerer resource serveren JWT-signaturen i produksjon (issuer-uri/JWKS)?
- Hvorfor bør autorisasjonsregler ligge så nær domenet som mulig (jf. case-09), og hva bør ligge i filterkjeden?
- Hvordan ville du satt opp dette mot Maskinporten i praksis (scopes, audience, token-utveksling)?

## Hvordan kjøre
```bash
mvn test -pl case-28-oauth2-jwt-resource-server
```
