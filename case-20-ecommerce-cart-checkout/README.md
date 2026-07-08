# Case 20 - Ecommerce cart checkout

## Domene
E-handel

## Tid
90 minutter

## Hva dette trener
- Aggregate Root
- Persistence boundary
- Transaction boundary
- Checkout-invariants

## Scenario
En handlekurv skal kunne få linjer, merge like produkter og sjekkes ut. Skeleton-koden har et aggregate og et use case, men mangler state, status og transaksjons-/persistensgrense.

## Oppgave
Modeller handlekurven som et aggregate som beskytter egne regler, og la `CheckoutCartUseCase` vise hvor application boundary ville ligget i en ekte løsning.

## TODO / fokusområder
- TODO: Implementer `addLine` med positivt antall og bevisst håndtering av samme produkt flere ganger.
- TODO: Hindre checkout av tom cart og dobbel checkout.
- TODO: La checkout endre tilstand og registrere tidspunkt på en testbar måte.
- TODO: Vurder repository-port/persistence som grense, men ikke bygg mer infrastruktur enn nødvendig.
- TODO: Forbedre testen som heter “should fail” slik at den faktisk forventer en kontrollert feil.

## Akseptansekriterier
- `Cart` har nok state til å håndheve egne invariants.
- Use case-laget orkestrerer, men eier ikke alle domenereglene.
- Tom cart og ugyldig quantity er dekket av tester.
- Det er tydelig hvor transaksjonsgrensen ville ligget i et Spring/JPA-prosjekt.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke implementer checkout som en tom metode. Caset handler om at aggregate og use case har ulike ansvar.

## Kommandoer

```bash
mvn -pl case-20-ecommerce-cart-checkout test
```
