# Case 05 - Parking pricing rules

## Domene
Parkering

## Tid
45-60 minutter

## Hva dette trener
- Domain Service
- Rounding
- Edge cases
- Tidsintervaller

## Scenario
En parkeringsøkt skal prises ut fra kjøretøytype, varighet og tariff. Testene starter med gratis parkering under 15 minutter, men resten av prisreglene må avklares og modelleres.

## Oppgave
Implementer en prisberegner som håndterer varighet, avrunding, rabatt/makspris og ugyldige tidsintervaller på en tydelig måte.

## TODO / fokusområder
- TODO: Behold regelen om gratis parkering under 15 minutter og legg tester rundt grenseverdien 15 minutter.
- TODO: Avklar avrunding: påbegynt time, nærmeste kvarter eller eksakt minuttpris.
- TODO: Modellér forskjell på `CAR`, `MOTORCYCLE` og `EV` uten å gjemme reglene i magiske tall.
- TODO: Bruk `nightMax` bevisst: når gjelder den, og hvordan kombineres den med ordinær pris?
- TODO: Valider at `endsAt` er etter `startsAt`.

## Akseptansekriterier
- Prisreglene kan forklares ut fra testnavn og kode.
- Grenseverdier rundt tid og avrunding er testet.
- `Money` håndteres med `BigDecimal` og forutsigbar skala.
- Løsningen er strukturert, men ikke et komplett parkeringssystem.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke implementer bare “under 15 minutter = 0”. Legg til nok regler til at avrunding og kjøretøytype må vurderes.

## Kommandoer

```bash
mvn -pl case-05-parking-pricing-rules test
```
