# Case 17 - Feature flag RBAC

## Domene
Plattform / feature flags

## Tid
45-60 minutter

## Hva dette trener
- Access policy
- SRP
- DIP
- Regelmatrise

## Scenario
Brukere med ulike roller skal kunne aktivere feature flags i ulike miljøer. Tilgang avhenger av rolle, produktområde, miljø og produksjonsgodkjenning.

## Oppgave
Implementer en testbar `FeatureFlagAccessPolicy` som uttrykker regelmatrisen tydelig. Målet er ikke å bygge et komplett feature flag-system, men å vise presise policy-regler.

## TODO / fokusområder
- TODO: Definer regler for `ADMIN`, `PRODUCT_OWNER` og `DEVELOPER` i `DEV`, `STAGING` og `PRODUCTION`.
- TODO: Bruk `productArea` aktivt der det er relevant, spesielt for ikke-admin-brukere.
- TODO: Håndter `approvedForProd` som en egen beslutningsfaktor for produksjon.
- TODO: Avklar om `experimental` flagg skal ha strengere regler.
- TODO: Skriv tester som dekker både tillatt og avvist tilgang for hver rollegruppe.

## Akseptansekriterier
- Policyen kan testes uten security-rammeverk.
- Regelmatrisen er lesbar i kode og tester.
- Produksjon er strengere enn lavere miljøer.
- Manglende produktområde gir ikke utilsiktet tilgang.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.

## Ikke gjør det for lett
Ikke la `ADMIN`-regelen skjule resten. Caset skal også vise hvordan du håndterer mer begrensede roller.

## Intervjuspørsmål / debrief
1. Hvordan holder du regelmatrisen lesbar i kode etter hvert som antall faktorer vokser?
2. Hvorfor er produksjon strengere enn lavere miljøer — og hvor ligger det i modellen?
3. Når lønner en regelmotor seg fremfor hardkodet policy?

## Kommandoer

```bash
./mvnw test -pl case-17-feature-flag-rbac
```
