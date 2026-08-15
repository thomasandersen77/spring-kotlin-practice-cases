# Case 43 – CurrentUser argument resolver

## Domene
Innlogget bruker i et profil-API med tenant og roller.

## Tid
60–75 minutter.

## Vanskelighetsgrad
Medium.

## Hva dette trener
- egen `@CurrentUser`-annotasjon
- `HandlerMethodArgumentResolver`
- mapping fra autentisert JWT-principal og claims
- et applikasjonsvennlig `AuthenticatedUser`-objekt
- registrering i Spring MVC
- isolert testing uten å spre Spring Security til domenet

## Scenario
Flere controllere trenger subject, tenant og roller fra samme autentiserte JWT. Direkte bruk av `SecurityContextHolder` eller `Jwt` i alle controllere skaper repetisjon og kobler applikasjonskoden til Spring Security.

## Oppgave
Implementer resolveren som injiserer `AuthenticatedUser` i parametre merket med `@CurrentUser`. Resolveren skal avvise manglende eller feil principal og ugyldige claims med tydelig semantikk.

## Gjeldende kontrakt
- Bare `AuthenticatedUser`-parametre med `@CurrentUser` støttes.
- Subject hentes fra JWT-standardclaimet `sub`.
- `tenant_id` må finnes og være ikke-blank.
- `roles` mappes til et read-only sett.
- Domenet og application service skal ikke kjenne `Jwt` eller `SecurityContextHolder`.

## TODO / fokusområder
1. Implementer presis `supportsParameter`.
2. Hent en `JwtAuthenticationToken` fra requestens principal.
3. Valider og map `sub`, `tenant_id` og `roles`.
4. Velg og test feilsemantikk for manglende principal/claims.
5. Verifiser at resolveren er registrert i MVC-konfigurasjonen.
6. Legg til én MockMvc-test som viser den ferdige controllerflyten.

## Forslag til arbeidsrekkefølge
1. Få `supportsParameter`-testen grønn.
2. Map en gyldig JWT i en ren resolvertest.
3. Dekk manglende og feilformede claims.
4. Test controllerintegrasjonen til slutt.

## Akseptansekriterier
- Resolveren støtter kun riktig annotasjon og type.
- Gyldige claims gir korrekt `AuthenticatedUser`.
- Ugyldig principal og claims feiler deterministisk.
- JWT-typen lekker ikke inn i domene-/servicelag.
- Ingen global eller trådlokal current-user-holder introduseres.
- Testene skiller mapping fra full MVC-integrasjon.

## Ikke gjør det for lett
Ikke les `SecurityContextHolder` direkte i domenet, injiser `Jwt` i alle controller-metoder eller bruk en global singleton for current user.

## Formål i treningen
Caset svarer konkret på spørsmålet «hvordan får jeg nåværende bruker elegant inn i controlleren?» og gir et godt utgangspunkt for å diskutere rammeverksgrenser.

## Treningsspørsmål / debrief
1. Hvorfor er argument resolver bedre enn gjentatt mapping i hver controller?
2. Hva er forskjellen på principal, JWT og intern current-user-modell?
3. Hvor bør ugyldige claims oversettes til HTTP-feil?
4. Hvorfor bør domenet ikke kjenne Spring Security?
5. Hvordan tester du resolveren uten full applikasjonskontekst?
6. Når er `@AuthenticationPrincipal` alene tilstrekkelig?

## Kommandoer
```bash
./mvnw test -pl case-43-current-user-argument-resolver
./mvnw verify -pl case-43-current-user-argument-resolver
```
