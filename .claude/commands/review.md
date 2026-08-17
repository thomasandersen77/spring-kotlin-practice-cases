---
description: Review og score et forsøk etter docs/TRENINGSGUIDE.md
argument-hint: <case-nummer> [branch]
---

Review-modus for case $1. Følg `AGENTS.md` § Arbeidsmodus punkt 2 og
scoringsreglene i `docs/TRENINGSGUIDE.md`.

**Ikke endre filer.** Dette er en vurdering, ikke en utførelse.

Gjør dette i rekkefølge:

1. `git branch --show-current` og `git status --short`
2. Finn case-modulen: `ls -d case-$1-*`
3. Les modulens `README.md` — akseptansekriteriene er målestokken
4. Les eksisterende tester og produksjonskoden
5. Finn branchens faktiske utgangspunkt og diff mot oppgaven:
   ```bash
   git diff main...HEAD -- <case-modul>
   git diff
   git diff --cached
   ```
6. Kjør `./mvnw test -pl <case-modul>` og bruk de faktiske resultatene

Rapporter:

- **Verifisert:** hva testene faktisk viser, med kommandoen du kjørte
- **Kontrakt:** hvilke akseptansekriterier som er oppfylt og hvilke som ikke er det
- **Konkrete styrker:** navngi dem, ikke generell ros
- **Konkrete trekk:** skill tydelig mellom bug, kontraktsbrudd, designvalg,
  Kotlin-idiomatikk og produksjonsforbedring utenfor casets krav
- **Prioriterte læringspunkter:** maks tre, viktigst først
- **Score 0.0–10.0** etter skalaen i `STATUS.md`, med begrunnelse

Trekk for manglende edge-tester, «Java med Kotlin-syntaks», domenelogikk på feil
sted og valg som ikke kan begrunnes. Legg til for eksplisitte invariants, tester
som leses som dokumentasjon og ryddig lagdeling.

Ufullstendige forsøk skal også scores. Ikke gjett muntlig forståelse — hvis
debrief ikke er gjennomført, si at scoren gjelder koden alene.

Ikke foreslå eller rediger `STATUS.md` med mindre brukeren ber om det.
