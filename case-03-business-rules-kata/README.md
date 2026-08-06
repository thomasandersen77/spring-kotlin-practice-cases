# Case 03 - Business rules kata

## Domene
Fakturering / rabattregler

## Tid
45-60 minutter

## Hva dette trener
- Forretningsregler
- `when`-logikk vs. regel/strategi-objekter
- Validering
- Edge case-tester

## Scenario
`InvoiceScoring` beregner subtotal, rabatt og total for en enkel faktura. VIP-rabatt og én rabattkode finnes allerede, men reglene for nye koder, stabling og validering er bevisst uavklarte.

## Oppgave
Utvid regellogikken på en måte som fortsatt er lett å lese, lett å teste og enkel å endre. Du skal kunne forklare hvordan du prioriterer mellom lesbarhet, fleksibilitet og tidsbruk.

## TODO / fokusområder
- TODO: Legg til støtte for flere rabattkoder uten at `score` blir en lang uoversiktlig metode.
- TODO: Avklar om VIP-rabatt og rabattkoder kan stables, og dokumenter prioriteten i tester.
- TODO: Valider tomme linjer, negative priser/antall og ukjente rabattkoder på en bevisst måte.
- TODO: Vurder om rabatter bør modelleres som regler/strategier eller enkel `when`-logikk.
- TODO: Legg til minst én test som beskriver en edge case, ikke bare happy path.

## Akseptansekriterier
- Subtotal, rabatt og total er tydelige begreper i koden.
- Nye rabattregler kan legges til uten stor risiko for regressjon.
- Testene viser både regelkombinasjoner og minst ett avklart feiltilfelle.
- Løsningen er liten nok til et intervju, men strukturert nok til å diskutere videre.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.

## Ikke gjør det for lett
Ikke gjør rabattreglene maksimalt generiske med en gang. Vis først en enkel, bevisst struktur som kan vokse.

## Intervjuspørsmål / debrief
1. Når ville du byttet fra `when`-logikk til regel- eller strategi-objekter?
2. Hvordan dokumenterer du rabatt-stabling og prioritet i tester?
3. Hvor hører validering av ukjent rabattkode hjemme — domene eller API?

## Kommandoer

```bash
./mvnw test -pl case-03-business-rules-kata
```
