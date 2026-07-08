# Case 14 - Insurance claim ACL

## Domene
Forsikring

## Tid
75-90 minutter

## Hva dette trener
- Anti-corruption layer
- DTO mapping
- Input validation
- Feilmodellering

## Scenario
Et eksternt skadesystem sender claim-data som strenger, nullable felter og leverandørspesifikke koder. Domenet skal få en trygg `InsuranceClaim` eller en forklarlig mapping-feil.

## Oppgave
Implementer `ClaimTranslator` som et anti-corruption layer. Den skal oversette og validere ekstern input uten at resten av domenet må kjenne til ekstern representasjon.

## TODO / fokusområder
- TODO: Map gyldige claim type-koder til `ClaimType`, og håndter ukjente/null-verdier uten tilfeldige exceptions.
- TODO: Parse og valider `incidentDate`; avklar om fremtidige datoer er lov.
- TODO: Vurder om `amount` og `currency` bør inn i domenemodellen eller bare valideres i ACL-et for dette caset.
- TODO: Returner `MappingResult.Failure` med nyttig melding for minst tre ulike feiltilfeller.
- TODO: Test at ekstern DTO ikke lekker videre som domeneobjekt.

## Akseptansekriterier
- Gyldig DTO blir `MappingResult.Success` med domenetype.
- Ugyldig DTO blir kontrollert failure, ikke uleselig parsing-crash.
- Mappingregler er samlet i ACL/translator.
- Domenet bruker egne begreper, ikke leverandørens felt-navn.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke bare kall `ClaimType.valueOf`. Caset handler om robust oversettelse, validering og tydelige feil.

## Kommandoer

```bash
mvn -pl case-14-insurance-claim-acl test
```
