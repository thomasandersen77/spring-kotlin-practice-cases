# Case 40 – Spring Security, interceptor og request context

## Scenario og tid
Et saks-API lar `USER` lese egne saker og `MANAGER` lese alle og godkjenne. Godkjenning lagrer audit i H2. Tidsboks: 75–90 minutter.

## Arkitektur
Spring Security eier sikkerhetsgrensen. JWT/principal mappes til en liten intern `CurrentUser`, som sendes eksplisitt til application service. Domenet kjenner ikke Spring. `ThreadLocal` brukes bare for correlation ID – aldri som skjult current-user-holder.

## Roller og endepunkter
- Alle `/api/**` krever JWT; manglende/ugyldig token gir 401.
- `USER` kan hente egen sak, ellers 403.
- `MANAGER` kan hente alle og `POST /api/cases/{id}/approve`.
- Correlation ID leses/genereres og returneres som `X-Correlation-ID`.

## TODO-er
1. Implementer holderens set/get/remove.
2. Implementer interceptor og alltid opprydding i `afterCompletion`.
3. Map JWT claims til `CurrentUser`.
4. Konfigurer moderne `SecurityFilterChain`, stateless JWT og `@EnableMethodSecurity`.
5. Beskytt manageroperasjon med `@PreAuthorize`.
6. Send `CurrentUser` eksplisitt til service.
7. Implementer eierskaps-/rollerregel uten Spring.
8. Lagre status og audit atomisk i service-transaksjonen.
9. Map til response-DTO.

## Request-livssyklus og risiko
Servlet-tråder gjenbrukes. Derfor må `ThreadLocal.remove()` alltid kalles, også ved exception; å sette `null` er ikke samme kontrakt. Kontekst følger ikke automatisk til coroutines, nye tråder eller `@Async`. Et servletfilter kan i produksjon være et bedre sted for korrelasjon fordi det omfatter mer av requestkjeden; caset bruker interceptor for å trene MVC-livssyklusen.

## Teststrategi
Ren domenetest, servicetest med fake repository, Security/MockMvc for 401/403/roller/eierskap, separat interceptortest for to sekvensielle requests og exception, `@DataJpaTest` for sak/audit og én ende-til-ende-flyt.

## Ikke gjør det for lett
Ikke les `SecurityContextHolder` i domenet, ikke legg brukeren i global `ThreadLocal`, ikke skriv tokenparser/kryptografi selv, og ikke bruk samme testannotasjon overalt.

## Debrief
Autentisering vs autorisasjon? 401 vs 403? Hvorfor eksplisitt `CurrentUser`? Hvorfor `remove()`? Interceptor vs filter? Hva skjer med coroutines? Hvordan kobles dette til en virkelig IdP?

## Kommando
`./mvnw test -pl case-40-security-request-context`
