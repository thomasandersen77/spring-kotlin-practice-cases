# Case 44 – JWT claims til authorities

## Domene
Faktura-API med lese-permissions og administratorrolle.

## Tid
60–90 minutter.

## Vanskelighetsgrad
Medium.

## Hva dette trener
- Spring Security Resource Server
- JWT-standardclaims og custom claims
- mapping til `GrantedAuthority`
- scope/permission kontra rolle
- 401 kontra 403
- målrettede security-tester

## Scenario
En ekstern identitetsleverandør utsteder token med `permissions` og `roles`. API-et skal forstå disse claimene uten å implementere egen tokenvalidering eller OAuth-provider.

## Oppgave
Implementer `JwtAuthorityMapper`, koble den til resource-server-konfigurasjonen og test både mappingen og HTTP-grensen.

## Gjeldende kontrakt
- `permissions: ["invoices:read"]` blir `SCOPE_invoices:read`.
- `roles: ["FINANCE_ADMIN"]` blir `ROLE_FINANCE_ADMIN`.
- Blanke verdier skal ikke gi authorities.
- Manglende/ugyldig token gir 401.
- Gyldig token uten nødvendig authority gir 403.
- GET krever lesetilgang; POST krever administratorrolle.

## TODO / fokusområder
1. Map permission- og role-claims med riktige prefikser.
2. Bestem hvordan feil type på et claim håndteres.
3. Fjern duplikate og blanke authorities deterministisk.
4. Koble mapperen til `JwtAuthenticationConverter`.
5. Legg til security-tester for 401, 403 og tillatt tilgang.
6. Test mapperen direkte uten å overteste JWT-kryptografi.

## Forslag til arbeidsrekkefølge
1. Få den rene mappertesten grønn.
2. Dekk manglende, blanke og duplikate claims.
3. Koble converteren til filterkjeden.
4. Avslutt med MockMvc-testene.

## Akseptansekriterier
- Custom claims blir korrekte Spring authorities.
- 401 og 403 brukes med riktig betydning.
- Mapperen har deterministisk oppførsel ved uventede claims.
- Tokenvalidering delegeres til Spring Security.
- Security-testene dekker autentisert, uautentisert og uautorisert bruker.
- Ingen OAuth-provider eller hjemmelaget kryptografi opprettes.

## Ikke gjør det for lett
Ikke legg inn ferdige authorities direkte i produksjonskoden, bruk `permitAll`, skriv egen JWT-parser eller bland roller og scopes uten eksplisitt navnekonvensjon.

## Formål i treningen
Caset lar deg forklare hele kjeden fra IdP-claim til autorisasjonsbeslutning uten å drukne i OAuth-provider-oppsett.

## Treningsspørsmål / debrief
1. Hva er forskjellen på authentication og authorization?
2. Når returnerer API-et 401, og når 403?
3. Hvorfor bruker Spring prefiksene `SCOPE_` og `ROLE_`?
4. Hvor bør custom claims mappes?
5. Hva tester `jwt()`-postprosessoren, og hva tester den ikke?
6. Hvordan håndterer du endringer i IdP-claimkontrakten?
7. Hvorfor bør API-et ikke validere signaturen selv?

## Kommandoer
```bash
./mvnw test -pl case-44-jwt-claims-authorities
./mvnw verify -pl case-44-jwt-claims-authorities
```
