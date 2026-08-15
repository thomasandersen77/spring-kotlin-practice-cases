# Treningsguide

Hvordan få mest mulig ut av dette repoet før teknisk trening.

## Treningsprotokoll per økt

1. **Velg case** fra root-README sin progresjon, eller et svakt case fra STATUS.md.
2. **Lag branch:** `git switch -c case-NN-forsoek-M main`.
3. **Sett tidsboks** etter casets `## Tid`. Stopp når tiden er ute — trening har også tidsboks.
4. **Les README-en grundig** før du åpner koden. Formuler oppgaven med egne ord, høyt.
5. **Kjør testene** (`./mvnw test -pl <modul>`) og les hva som er rødt. Røde tester er kontrakten, ikke fienden.
6. **Skriv kontrakttester først** der caset ber om det (TDD). Navngi tester slik at de dokumenterer regelen.
7. **Implementer stegvis.** Tenk høyt hele veien: "Nå legger jeg invarianten her fordi ..."
8. **Refaktorer til slutt** — ikke optimaliser for tidlig.
9. **Debrief høyt:** svar på casets `## Treningsspørsmål / debrief` som om coachen sitter ved siden av deg. Bruk maks 2 minutter per spørsmål.
10. **Score og registrer** i STATUS.md før du går videre.

## Tenk-høyt-regelen

I treningen vurderes resonneringen din like mye som koden. Øv derfor alltid på å si høyt:

- hva du gjør nå og hvorfor
- hvilke alternativer du vurderer
- hvilken trade-off du tar, og hvorfor den er riktig *for dette tilfellet*
- hva du ville gjort annerledes i et ekte prosjekt

Hvis du ikke kan si noe fornuftig høyt om et valg, er valget sannsynligvis ikke bevisst nok ennå.

## Scoringsveiledning

Se skalaen i STATUS.md. Trekk poeng for:

- manglende edge-tester (README lister dem vanligvis)
- valg du ikke kan begrunne i debrief
- tidsoverskridelse (> 25 % over tidsboksen)
- "Java med Kotlin-syntaks" der idiomatisk Kotlin var naturlig

Legg til for:

- eksplisitte invariants i stedet for kommentarer
- tester som leses som dokumentasjon
- ryddig lagdeling (domene uten Spring/JPA-lekkasje)

**Vær ærlig.** En 6.5 du kan forsvare er mer verdt enn en 9.0 du ga deg selv for å føle deg ferdig.

## Iterativ trening: hvorfor branches per forsøk

Andre og tredje gjennomkjøring av samme case er der læringen sitter. Første gang løser du problemet; andre gang løser du det *ryddigere og raskere*; tredje gang *forklarer* du det uanstrengt.

- Forsøk 1: få det til å virke. Score typisk 5–7.
- Forsøk 2 (dager/uker senere, fra `main`): fokus på testdekning og idiomatikk. Mål: slå forrige score.
- Forsøk 3: simulér trening — tidsboks, tenk høyt, debrief. Mål: 9+.

Sammenlign alltid med `git diff main...HEAD` for å se hva du faktisk endret, og med forrige branch for å se fremgang.

## Regler for repoet

1. **Originaloppgaven på `main` skal alltid kompilere.** Røde tester er OK når de beskriver kontrakten — kompileringsfeil, manglende dependencies og config-feil er ikke OK. Friksjon skal være domenet, ikke verktøyet.
2. **Ingen ferdige løsninger på `main`.** Løsninger lever på `case-NN-forsoek-M`-branches.
3. **README-malen er hellig:** Domene, Tid, Hva dette trener, Scenario, Oppgave, TODO / fokusområder, Akseptansekriterier, Formål i treningen, Ikke gjør det for lett, Treningsspørsmål / debrief, Kommandoer. Nye caser følger samme mal.
4. **Tester skal beskrive kontrakten** med beskrivende navn. Ingen `test1`, ingen tomme testmetoder uten assertions.
5. **"Ikke gjør det for lett"-seksjonen skal respekteres.** Den beskriver snarveien som ødelegger læringsutbyttet.
6. **STATUS.md er sanningen** om fremgangen. Oppdater den etter hvert forsøk, ikke i slutten av uken.

## Kveldsøkt på 60 minutter (eksempel)

- 5 min: velg case, lag branch, les README høyt
- 40 min: implementer med tenk-høyt-protokollen
- 10 min: debrief-spørsmål høyt
- 5 min: score og STATUS.md
