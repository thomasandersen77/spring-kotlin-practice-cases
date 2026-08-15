# Case 45 – Method security og domenerelevant autorisasjon

## Domene
Dokumenttilgang basert på eierskap, klassifisering og rolle.

## Tid
45–60 minutter.

## Vanskelighetsgrad
Medium.

## Hva dette trener
- `@EnableMethodSecurity` og `@PreAuthorize`
- roller og domenerelevante tilgangsregler
- access policy som egen samarbeidspartner
- forskjellen på autentisering og autorisasjon
- testing av tillatt og avvist bruker
- grensen mellom uttrykk og Kotlin-kode

## Scenario
Interne dokumenter kan leses av eier og reviewer. Konfidensielle dokumenter krever auditor-rolle. Regelen avhenger av både innlogget bruker og ressursdata, og er derfor mer enn en enkel rollecheck.

## Oppgave
Implementer `DocumentAccessPolicy`, koble den til metodeautorisasjonen og test både policyen og den proxiede servicegrensen.

## Gjeldende kontrakt
- Eier kan lese eget `INTERNAL`-dokument.
- `REVIEWER` kan lese alle interne dokumenter.
- `CONFIDENTIAL` krever `AUDITOR`, også for eier.
- Manglende dokument skal ikke forveksles med manglende tilgang uten et bevisst valg.
- Uautorisert metodekall avvises før serviceoperasjonen fullføres.

## TODO / fokusområder
1. Implementer policyens eierskaps-/rollerregel.
2. Velg bevisst semantikk for ukjent dokument.
3. Hold SpEL-uttrykket kort og flytt domeneregelen til Kotlin.
4. Test policyen isolert.
5. Test `@PreAuthorize` gjennom en Spring-proxy med `@WithMockUser`.
6. Dekk konfidensiell ressurs som egen gren.

## Forslag til arbeidsrekkefølge
1. Få rene policytester grønne.
2. Legg til klassifiseringsgrensen.
3. Test serviceproxyen og `AccessDeniedException`.
4. Refaktorer uttrykket dersom det beskriver for mye domene.

## Akseptansekriterier
- Alle tilgangsreglene er eksplisitt testet.
- `@PreAuthorize` er aktivert og treffer proxied service.
- SpEL inneholder ikke en hel regelmatrise.
- Policyen kan testes uten web- eller JWT-oppsett.
- Authentication og authorization omtales presist i debrief.
- Ingen controller-only-sikring brukes som eneste barriere.

## Ikke gjør det for lett
Ikke hardkod `true`, legg alle reglene i en lang SpEL-streng eller test bare policyklassen uten å bevise at method security faktisk er aktiv.

## Formål i treningen
Caset viser hvordan enkel rolleautorisasjon utvikler seg til en ressursbasert policy, og hvor Spring bør slutte og domenespråket begynne.

## Treningsspørsmål / debrief
1. Hvorfor er `@PreAuthorize` på service nyttig selv om controlleren er sikret?
2. Når blir SpEL-uttrykket for komplekst?
3. Hvordan skiller du 404 fra 403 uten å lekke ressursinformasjon?
4. Hva kreves for at method security skal gå gjennom en proxy?
5. Hva er fordelen med en egen access policy?
6. Hvordan tester du både policyen og Spring-integrasjonen?

## Kommandoer
```bash
./mvnw test -pl case-45-method-security-authorization
./mvnw verify -pl case-45-method-security-authorization
```
