# Case 02 - Debugging og testbarhet

## Domene
Kapasitetsplanlegging

## Tid
45-60 minutter

## Hva dette trener
- Debugging
- Kontrakttester / TDD
- Dato-semantikk (inclusive/exclusive)
- Refaktorering mot domenespråk

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

## Intervjuspørsmål / debrief
1. Hvilken semantikk valgte du for `to` – inklusiv eller eksklusiv – og hvorfor?
2. Hva er forskjellen mellom datokontrakten og implementasjonsdetaljene som realiserer den?
3. Hva skal skje når `from > to` – exception, `0` eller et eksplisitt resultat – og hvorfor?
4. Hvordan modellerte du begrepene arbeidsdag og helg, og hvilke alternativer vurderte du?

## Kommandoer

```bash
mvn test -pl case-02-debug-and-test
```
