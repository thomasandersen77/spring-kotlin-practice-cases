# Case 23 - Optimistic locking concurrency
## Domene
Samtidighet / lagerreservasjon
## Tid
60-75 minutter
## Hva dette trener
- Optimistic locking
- Aggregate versionering
- Konflikthåndtering uten globale låser
- Tydelig applikasjonsgrense mellom domene og repository-port
## Scenario
To brukere reserverer samme vare nesten samtidig. Begge leser samme versjon av aggregate, men bare én oppdatering skal vinnes. Den andre må få en kontrollert konflikt, ikke stille overskriving.
## Oppgave
Implementer et lite reserve-use-case med versjonskontroll. Skill domenevalidering (kan vi reservere?) fra samtidighetsvalidering (fikk vi lagret på forventet versjon?).
## TODO / fokusområder
- TODO: Modellér versjon i aggregate og oppdater versjon ved gyldig tilstandsendring.
- TODO: Definér eksplisitt resultat for konflikt (`CONFLICT`) og domenebrudd (`REJECTED`), ikke bare exceptions.
- TODO: Hold repository som port med expectedVersion i save-kallet.
- TODO: Skriv test som dokumenterer hva som skjer ved stale version.
- TODO: Forklar når optimistic locking er bedre enn pessimistic locking i dette domenet.
## Akseptansekriterier
- Gyldig reservasjon reduserer tilgjengelig antall og øker versjon.
- Konkurrerende oppdatering på gammel versjon gir kontrollert konflikt.
- Domeneregel for ugyldig quantity håndteres tydelig.
- Koden kan forklares med aggregate + application service + port.
## Formål i intervjuet
Målet er å vise at du kan modellere konsistens under samtidighet uten å overdesigne infrastrukturen. Du skal kunne forklare hvilke feil som er domenefeil, hvilke som er konkurransefeil, og hvordan resultatene bør eksponeres oppover i lagene.
## Ikke gjør det for lett
Ikke “løs” konflikten ved å hente og overskrive på nytt uten diskusjon. Caset handler om bevisst konfliktstrategi.
## Kommandoer
```bash
mvn -pl case-23-optimistic-locking-concurrency test
```
