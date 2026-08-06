# Case 11 - Hospital triage policy

## Domene
Helse / triage

## Tid
45 minutter

## Hva dette trener
- Domain Service
- Business rules
- Guard clauses
- Regelprioritet

## Scenario
En triage-policy skal prioritere pasienter basert på symptomalvorlighet, vitale tegn, smerte, alder og ventetid. Testene viser bare starten på regelsettet.

## Oppgave
Implementer en lesbar policy der de viktigste reglene evalueres først. Du skal kunne forklare hvorfor regelrekkefølgen er valgt, og hvordan du ville utvidet reglene med flere medisinske kriterier.

## TODO / fokusområder
- TODO: Start med tester for kritiske symptomer og farlige vitale tegn før du fyller ut resten.
- TODO: Avklar terskler for lav oksygenmetning, høy feber, høy smerte og lang ventetid.
- TODO: Bestem om alder skal eskalere prioritet alene eller bare sammen med andre funn.
- TODO: Valider ugyldige inputverdier, som negativ alder eller pain score utenfor valgt skala.
- TODO: Hold policyen deterministisk: samme request skal alltid gi samme prioritet.

## Akseptansekriterier
- `IMMEDIATE`, `URGENT` og `STANDARD` er dekket av tester.
- Regelrekkefølgen er tydelig i koden.
- Edge cases håndteres med guard clauses eller tydelige domeneregler.
- Løsningen er enkel nok for intervju, men ikke bare én stor ubegrunnet `if`.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.

## Ikke gjør det for lett
Ikke la første test definere hele policyen. Legg inn nok regler til at prioritet og konflikter må diskuteres.

## Intervjuspørsmål / debrief
1. Hvorfor evalueres kritiske symptomer først — hva er konsekvensen av feil rekkefølge?
2. Hvordan holder du policyen deterministisk og testbar?
3. Når eskalerer alder prioritet — alene eller bare kombinert med andre funn?

## Kommandoer

```bash
./mvnw test -pl case-11-hospital-triage-policy
```
