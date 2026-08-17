---
description: Lesende Git- og testkontroll av lokal repo-tilstand (KONTROLL_WARP)
argument-hint: [case-nummer]
allowed-tools: Bash(git status:*), Bash(git branch:*), Bash(git fetch:*), Bash(git log:*), Bash(git diff:*), Bash(git merge-base:*), Bash(git worktree list), Bash(./mvnw:*), Read, Glob, Grep
---

Kjør `KONTROLL_WARP` fra `docs/AGENTS-WORKFLOW.md`. Dette er **rent lesende
kontroll**. Ikke rediger filer, ikke bytt branch, ikke commit, push, merge,
rebase eller reset — heller ikke hvis noe ser feil ut.

Rapporter faktisk tilstand:

```bash
git branch --show-current
git status --short
git fetch origin
git log --oneline -5
```

Sammenlign mot branchens faktiske utgangspunkt når vi står på en
forsøks- eller fasitbranch:

```bash
BASE=$(git merge-base main HEAD)
git diff --stat "$BASE"..HEAD
```

Sjekk også staged og ucommittede endringer separat (`git diff`,
`git diff --cached`).

Hvis $1 er oppgitt: avgrens diff og testkjøring til den case-modulen og kjør
`./mvnw test -pl <case-modul>`. Ellers kjør repoets friskhetssjekk
`./mvnw clean test-compile -DskipTests`.

Rapporter:

- branch og om den stemmer med `case-NN-forsoek-M`-konvensjonen
- rent eller skittent working tree, med hvilke filer
- avvik mellom lokal branch og `origin`
- faktiske testresultater, ikke antatte
- eventuelle avvik fra reglene i `AGENTS.md`

Ikke hallusiner testresultater eller branchtilstand. Hvis en kommando ikke kan
kjøres, si det og reduser sikkerheten i vurderingen.
