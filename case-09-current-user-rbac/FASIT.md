# Fasit – CurrentUser og tilgangskontroll

`CaseService` er use case-grensen der current user, lastet sak og `AccessPolicy` møtes. Controlleren bruker bare path-parameteren som case-id og mapper resultatet. `CaseRepository` gjør flyten testbar uten database eller SecurityContext.

Bare en åpen sak kan lukkes. Admin kan lukke enhver åpen sak; read-only kan aldri; case worker må tilhøre samme organisasjon. Manglende tilgang gir `CaseAccessDeniedException`, og `CustomerCase.close` beskytter state-overgangen.

Testene dekker alle roller, organisasjon, lukket status, autorisert persistence og avvist use case. En JWT-adapter ville mappet claims til `User` før controller/use case uten at policyen kjenner Spring Security.

Kort intervjuforklaring: autentisering er adapteransvar; autorisasjon er en eksplisitt policy nær application/domain-grensen.
