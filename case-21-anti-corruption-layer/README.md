# Case 21 - Anti-corruption layer

## Domene
Kredittvurdering / integrasjon

## Tid
60-75 minutter

## Hva dette trener
- Anti-corruption layer
- Oversettelse av ekstern modell
- Policy-testing
- Domenespråk

## Scenario
En ekstern kredittleverandør returnerer tekniske/legacy-pregede felter som `score_value`, `red_flag` og `source_system`. Domenet skal snakke om `CreditRisk`, `LoanApplication` og `CreditDecision` i stedet.

## Oppgave
Fullfør anti-corruption layeret og domenepolicyen. Hold ekstern mapping adskilt fra beslutningsreglene, og gjør det tydelig hvilke regler som er leverandørspesifikke og hvilke som er forretningsregler.

## TODO / fokusområder
- TODO: Test oversettelsen fra ekstern score/red flag til `CreditRisk`, inkludert lave scores og `red_flag`.
- TODO: Implementer `CreditPolicy` uten at den kjenner til ekstern DTO eller `source_system`.
- TODO: Koble `LoanApplicationService` inn som Spring service, men behold policy og translator som lett testbare enheter.
- TODO: Avklar hvordan manuell behandling skal uttrykkes med dagens `CreditDecision`-modell, eller forbedre modellen uten å gjøre caset for stort.
- TODO: Legg inn minst én negativ test som viser at høy risiko ikke behandles som en teknisk feil.

## Akseptansekriterier
- Eksterne DTO-er lekker ikke inn i domain/core.
- Mapping og policy kan testes separat.
- Service-laget orkestrerer provider, translator og policy uten å eie selve forretningsreglene.
- README, kode og tester bruker samme domenespråk.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.

## Ikke gjør det for lett
Ikke hardkod hele beslutningen direkte i controller/service. Skill mellom oversettelse av ekstern data og intern kredittpolicy.

## Intervjuspørsmål / debrief
1. Hva er forskjellen på oversettelse (translator) og beslutning (policy)?
2. Hvorfor skal `CreditPolicy` ikke kjenne `source_system`?
3. Hvorfor er høy risiko ikke en teknisk feil?

## Kommandoer

```bash
mvn test -pl case-21-anti-corruption-layer
mvn spring-boot:run -pl case-21-anti-corruption-layer
```
