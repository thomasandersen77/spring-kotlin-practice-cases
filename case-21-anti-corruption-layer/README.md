# Case 21 - Anti-corruption layer

Øv på en ekstern kredittintegrasjon der leverandørens DTO-er og felt-navn ikke skal bli en del av domenespråket.

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

## Hvordan kjøre

```bash
mvn test
mvn spring-boot:run
```
