# Case 02 - Debugging og testbarhet

Finn og avklar subtil datologikk i en eksisterende kapasitetsberegner før du refaktorerer.

## Scenario
`CapacityPlanner` teller tilgjengelige arbeidsdager mellom to datoer, men koden har bevisst tvetydighet og minst én off-by-one-felle. Den håndterer også helger og fraværsdager på en enkel måte.

## Oppgave
Skriv tester som beskriver ønsket oppførsel før du endrer implementasjonen. Refaktorer deretter til lesbar kode med tydelig dato-semantikk.

## TODO / fokusområder
- TODO: Avklar om `to` skal være inclusive eller exclusive, og la testnavnene dokumentere valget.
- TODO: Test `from == to`, helg i start/slutt, fravær på helg og fravær utenfor perioden.
- TODO: Bestem hva som skal skje hvis `from` er etter `to`.
- TODO: Refaktorer løkken slik at arbeidsdag/fravær blir egne lesbare begreper.
- TODO: Hold controlleren enkel; hovedinteressen er domenelogikk og testbarhet.

## Akseptansekriterier
- Testene beskriver dato-kontrakten før implementasjonen endres.
- Off-by-one-regelen er forståelig for en reviewer.
- Helg og fravær behandles konsekvent.
- Refaktoreringen gjør koden lettere å lese, ikke bare kortere.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke endre forventede tall tilfeldig til testene passer. Start med å formulere kontrakten, og gjør deretter koden konsistent med den.

## Hvordan kjøre

```bash
mvn test
mvn spring-boot:run
```
