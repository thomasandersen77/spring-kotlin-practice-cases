# Case 12 - Payment settlement strategy

## Domene
Betaling / oppgjør

## Tid
60 minutter

## Hva dette trener
- OCP
- Strategy Pattern
- DIP
- Money-beregning

## Scenario
Ulike betalingsmetoder har ulike oppgjørsgebyrer. `SettlementCalculator` skal bruke strategier i stedet for å kjenne alle gebyrreglene direkte.

## Oppgave
Bygg en liten strategi-basert løsning for gebyrberegning. Den skal være enkel å utvide med nye betalingsmetoder, men fortsatt lett å lese i et intervju.

## TODO / fokusområder
- TODO: Lag konkrete strategier for relevante betalingsmetoder, inkludert minst kortbetaling som testene starter med.
- TODO: Bestem hva som skjer når en strategi mangler: exception, fallback eller eksplisitt resultat.
- TODO: Avklar avrunding og skala for `Money` ved prosentbaserte gebyrer.
- TODO: Test at riktig strategi velges, ikke bare at ett tall returneres.
- TODO: Vurder om `SettlementCalculator(emptyList())` i testene skal endres eller om calculatoren skal ha trygge defaults.

## Akseptansekriterier
- Ny betalingsmetode kan legges til uten å endre en stor `when` i calculatoren.
- Gebyrregler er isolert og testbare.
- Manglende/ukjent betalingsmetode håndteres bevisst.
- Money-beregninger bruker `BigDecimal` på en forutsigbar måte.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.

## Ikke gjør det for lett
Ikke hardkod alle gebyrer direkte i `calculateFee`. Poenget er å vise hvordan en strategi-grense kan se ut i liten skala.

## Intervjuspørsmål / debrief
1. Hvordan legger du til en ny betalingsmetode uten å endre calculatoren?
2. Strategy Pattern vs. sealed class + `when` — når velger du hva?
3. Hva skjer ved ukjent betalingsmetode, og hvorfor valgte du det?

## Kommandoer

```bash
mvn test -pl case-12-payment-settlement-strategy
```
