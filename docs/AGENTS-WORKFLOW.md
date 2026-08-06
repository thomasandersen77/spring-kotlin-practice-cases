**# Arbeidsflyter for agenter

## FASIT_CODEX

Brukes bare ved eksplisitt bestilling av fasit.

1. Les `AGENTS.md`, case-README, produksjonskode og eksisterende tester.
2. Kontroller repository, working tree og remote.
3. Kjør `git fetch origin`.
4. Verifiser at valgt startpunkt tilsvarer `origin/main`.
5. Opprett `case-NN-fasit` i Codex-worktree.
6. Endre bare aktuell case-modul.
7. Ikke endre kontrakt eller eksisterende tester for å skjule feil.
8. Implementer en korrekt, lesbar og pragmatisk fasit.
9. Opprett `<case-modul>/FASIT.md` med:
    - kontrakt og domeneregler
    - Kotlin- og designvalg
    - alternativer og trade-offs
    - tester og edge cases
10. Kjør modulens `test` og `verify`.
11. Kjør repositoryets dokumenterte compile-kontroll.
12. Commit og push bare når dette er eksplisitt tillatt i prompten.
13. Ikke opprett PR, merge eller endre `STATUS.md`.

## KONTROLL_WARP

Warp er som standard en rent lesende Git-kontrollør.

Tillatt:

- lese filer og Git-status
- kjøre `git fetch`
- liste worktrees og branches
- kjøre Maven-tester
- sammenligne branches og commits
- rapportere avvik

Ikke tillatt uten eksplisitt beskjed:

- redigere filer
- bytte branch
- committe eller pushe
- merge, rebase, reset eller slette
- endre eller fjerne worktrees

## SAMMENLIGN

1. Sammenlign `case-NN-forsoek-M` med `case-NN-fasit`.
2. Les kontrakt og tester før diffen vurderes.
3. Ikke kopier fasit inn i forsøket.
4. Skill mellom:
    - korrekthetsfeil
    - kontraktsbrudd
    - Kotlin-idiomatikk
    - designvalg
    - produksjonsforbedringer utenfor tidsboksen
5. Gi først observasjoner og spørsmål, deretter eventuelle hint.**