# Case 11 - Library loan domain

## Domene
Bibliotek

## Tid
30-45 minutter

## Hva dette trener
- Entity
- Value Object
- Domain rules
- Dato-/statuslogikk

## Scenario
Et biblioteklån har bok, låner, låneperiode og status. Koden validerer allerede perioden, men regler for forlengelse og forfalt lån mangler.

## Oppgave
Gjør `Loan` til et lite domeneobjekt med tydelige regler. Du må selv velge hvordan du modellerer om et lån allerede er forlenget, og hvordan returnerte lån skal behandles.

## TODO / fokusområder
- TODO: Implementer forlengelse kun for aktive lån, og avklar hvordan “én gang” skal lagres i modellen.
- TODO: Valider at forlengelsesdager er positive og innenfor en rimelig grense.
- TODO: Implementer `overdueDays` slik at datoer før/lik forfallsdato ikke gir negativt resultat.
- TODO: Legg til tester for returnert lån, andre gangs forlengelse og forfalt/ikke-forfalt lån.
- TODO: Vurder om metoder skal returnere ny `Loan` eller mutere intern state, og forklar valget.

## Akseptansekriterier
- Låneperiodens invariant beholdes etter forlengelse.
- Status påvirker hva som er lov.
- Overdue-beregningen er enkel å lese og testet med kantdatoer.
- Modellen viser tydelig hva som er entity og value object.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke skjul “forlenget én gang”-regelen i en kommentar. Modellen må bære nok state til at regelen kan håndheves.

## Kommandoer

```bash
mvn -pl case-11-library-loan-domain test
```
