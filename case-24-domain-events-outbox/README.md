# Case 24 - Domain events outbox

## Domene
Order / integrasjon

## Tid
75-90 minutter

## Hva dette trener
- Domain events
- Transactional outbox
- SRP mellom domene, applikasjonslag og publisering
- Feiltoleranse i integrasjonsnære use cases

## Scenario
Når en ordre plasseres skal systemet både persistere ordren og registrere en publiserbar melding for videre behandling. Hvis bare den ene operasjonen skjer, får du inkonsistens mellom intern state og integrasjoner.

## Oppgave
Lag en tydelig use case der ordre blir lagret og `OrderPlaced` blir lagt i outbox. Hold domenet uavhengig av transportformat, og modeller melding i adapter-/outbox-laget.

## TODO / fokusområder
- TODO: Definér `OrderPlaced` som domeneevent uten teknisk metadata som topic/partition.
- TODO: Oversett domeneevent til `OutboxMessage` i et eget steg.
- TODO: Gjør transaksjonsgrensen eksplisitt i use case (ordre + outbox i samme enhet).
- TODO: Avklar hva som skjer hvis outbox-write feiler etter ordrevalidering.
- TODO: Skriv minst én test som viser mapping fra domeneevent til outbox-melding.

## Akseptansekriterier
- Domenet produserer event som uttrykker forretningshendelse.
- Outbox-melding er teknisk representasjon av eventet, ikke omvendt.
- Use case viser hvor atomisitet er nødvendig.
- Koden er liten, men tydelig nok til å diskutere drift/robusthet.

## Formål i treningen
Målet er å vise at du kan beskytte konsistens på tvers av intern modell og ekstern integrasjon uten å dra inn full infrastruktur. Du skal kunne forklare hvorfor outbox finnes, og hva som går galt uten den.

## Ikke gjør det for lett
Ikke publiser event direkte i use case uten mellomlag. Caset handler om kontrollert overgang fra domeneevent til leveringsmekanisme.

## Treningsspørsmål / debrief
1. Hva går galt uten outbox? Beskriv et konkret scenario.
2. Hvorfor er `OutboxMessage` en teknisk representasjon og ikke domeneeventet selv?
3. Hvor er atomisiteten, og hva skjer hvis outbox-write feiler?

## Kommandoer

```bash
./mvnw test -pl case-24-domain-events-outbox
```
