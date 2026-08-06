# Fasit – Spring Security, interceptor og request context

Alle `/api/**` krever stateless JWT. Roles-claim mappes til `ROLE_*`, og manager-approve er beskyttet med `@PreAuthorize`. JWT mappes til intern `CurrentUser`, som sendes eksplisitt til service; USER kan lese egen sak, MANAGER alle.

Correlation-interceptoren gjenbruker/genererer id, setter response-header og kaller alltid `ThreadLocal.remove()` i `afterCompletion`. Current user lagres aldri skjult. Approve endrer status og lagrer audit i samme transaksjon og avviser manglende manager eller gjentatt godkjenning.

Teststrategien dekker ren policy/context, service, JPA og MockMvc 401/403/eierskap/manager. I produksjon bør klokke injiseres, audit være append-only og correlation ofte ligge i et filter som omfatter hele requestkjeden.
