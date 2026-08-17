# Claude Code — Kotlin Training Cases

Regelverket for dette repoet ligger i `AGENTS.md` og er felles for alle agenter
(Claude Code, Codex, Warp, ChatGPT). Denne filen legger bare til det som er
spesifikt for Claude Code. Ved motstrid gjelder `AGENTS.md`.

@AGENTS.md

@docs/AGENTS-WORKFLOW.md

@docs/TRENINGSGUIDE.md

## Claude-spesifikt

### Bygg og kjøring

- Bruk alltid `./mvnw`, aldri en globalt installert `mvn`.
- **Java 21 er påkrevd.** Kotlin 1.9.25 bygger ikke på JDK 22+. Kjør `sdk env` i
  repo-roten først. Feil JDK stoppes av `maven-enforcer-plugin`.
- `./mvnw test` på `main` skal være **rødt**. Casene er uferdige med vilje og
  kaster `NotImplementedError` fra `TODO()`. Ikke rapporter dette som en feil i
  repoet og ikke «fiks» det.
- Friskhetssjekk av repoet er `./mvnw clean test-compile -DskipTests`. Den skal
  alltid være grønn.
- Ett case: `./mvnw test -pl <case-modul>`.

### Modus

Coach-modus er standard, også når du blir bedt om å «se på» eller «forklare» et
case. Ikke skriv kopierbar løsningskode for uløste `TODO()` uten at brukeren
eksplisitt bestiller fasit eller implementering. Se hintnivå 1–3 i `AGENTS.md`.

### Verktøybruk i Claude Code

- **Ikke bruk subagenter (Task/Agent) til å løse eller fasitere caser.** En
  subagent har ikke coach-kontrakten i konteksten sin og leverer ferdig kode.
  Subagent er greit til rent lesende arbeid: søke etter mønstre på tvers av
  moduler, kartlegge testdekning, finne relevante filer.
- Les alltid casets `README.md` og eksisterende tester før du kommenterer
  kontrakten. Ikke gjett innholdet.
- Ikke bruk `Write` på filer i en case-modul i coach-modus.
- `docs/AI-ARBEIDSMETODIKK.md` er personlig metodikk og leses ved behov, ikke
  automatisk.

### Tillatelser

`.claude/settings.json` tillater `./mvnw` og lesende Git. Alt som endrer
repository-tilstand — `git switch`, `add`, `commit`, `push`, `merge`, `rebase`,
`reset`, `gh pr` — spør hver gang. Det er med vilje: `AGENTS.md` krever separat
eksplisitt tillatelse for hvert steg, og en tillatelse gjelder bare den ene
handlingen i den ene samtalen.

`.claude/settings.local.json` er personlig og ignoreres av Git. Legg
maskinspesifikke overstyringer der, ikke i `settings.json`.

### Slash-kommandoer

| Kommando | Modus |
|---|---|
| `/coach <NN>` | Coach-modus med graderte hint |
| `/kontroll` | Lesende Git- og testkontroll (`KONTROLL_WARP`) |
| `/review <NN>` | Review og score etter `docs/TRENINGSGUIDE.md` |
| `/sammenlign <NN>` | Diff forsøk mot oppgave og eventuell fasit (`SAMMENLIGN`) |
| `/nytt-forsoek <NN>` | Opprett `case-NN-forsoek-M` fra ren `main` |
| `/fasit <NN>` | Fasitmodus (`FASIT_CODEX`) — bare ved eksplisitt bestilling |
