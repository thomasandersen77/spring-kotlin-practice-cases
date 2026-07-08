# Case 07 - Order API via core-lag til database

Bygg videre på et lite ordre-API der en ordre er aggregatrot, ordrelinjer lever innenfor ordren, og JPA kun er en persistensdetalj.

## Scenario
Du overtar en nesten fungerende Spring Boot-modul. Controller, service, repository-port, JPA-entity og domenemodell finnes allerede, men oppgaven er å stramme inn grensene og gjøre domenereglene tydelige.

## Oppgave
Fullfør og forbedre løsningen slik at ordre kan opprettes, hentes, bekreftes og kanselleres uten at HTTP/JPA-detaljer lekker inn i domenet. Tenk høyt rundt hvor validering, mapping og transaksjonsgrenser bør ligge.

## TODO / fokusområder
- TODO: Avklar og test hvilke invariants `Order` og `OrderLine` skal håndheve, blant annet tom ordre, ugyldig antall og eventuelle prisgrenser.
- TODO: Vurder om statusovergangene `PENDING -> CONFIRMED` og `PENDING/CONFIRMED -> CANCELLED` er riktige, og legg tester rundt ulovlige overganger.
- TODO: Hold controlleren tynn: request/response-mapping i API-laget, use case-orkestrering i service og rene regler i domenet.
- TODO: Forklar hvorfor `OrderRepository` er en port, og hva som er fordelen/ulempen med JPA-mappingen som ligger i adapterlaget.
- TODO: Legg til minst én test som viser at domenet kan testes uten Spring-kontekst.

## Akseptansekriterier
- Domenemodellen kan forklare hvorfor en ordre er gyldig eller ugyldig.
- Service-laget eier transaksjonsgrensen og returnerer domenetyper, ikke JPA-entities.
- Repository-adapteren skjuler Spring Data/JPA fra resten av applikasjonen.
- Tester dekker minst én positiv flyt og én viktig negativ domeneregel.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke løs alt med annotations i controlleren. Noe inputvalidering hører hjemme i API-laget, men forretningsreglene skal være testbare uten HTTP, JSON eller database.

## Hvordan kjøre

```bash
mvn test
mvn spring-boot:run
```
