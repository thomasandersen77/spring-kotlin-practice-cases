---
description: Fasitmodus — referanseløsning på case-NN-fasit (krever eksplisitt bestilling)
argument-hint: <case-nummer>
---

Fasitmodus for case $1. Dette er en **eksplisitt bestilling av fasit**. Følg
`FASIT_CODEX` i `docs/AGENTS-WORKFLOW.md` og `AGENTS.md` § Arbeidsmodus punkt 4.

Arbeidsflyten er verktøynøytral og kjøres i én primær checkout med brancher,
ikke separate worktrees.

**Steg:**

1. Les `AGENTS.md`, case-modulens `README.md`, produksjonskoden og eksisterende
   tester.
2. Kontroller branch og working tree:
   ```bash
   git status --short
   git branch --show-current
   git fetch origin
   ```
   Er working tree skittent: stopp og spør før du bytter branch.
3. Bytt til ren baseline og opprett fasitbranchen fra verifisert `origin/main`:
   ```bash
   git switch main
   git pull --ff-only origin main
   git switch -c case-$1-fasit origin/main
   ```
4. **Endre bare den aktuelle case-modulen.**
5. Implementer en korrekt, lesbar og pragmatisk referanseløsning. Følg
   kontrakten og tidsboksen — ikke overimplementer et hypotetisk
   produksjonssystem.
6. Ikke endre case-README, offentlig kontrakt eller eksisterende tester for å
   skjule feil. Legg bare til nødvendige kontrakt- eller edge-case-tester.
7. Opprett `<case-modul>/FASIT.md` med:
   - kontrakt og domeneregler
   - Kotlin- og designvalg
   - alternativer og trade-offs
   - tester og edge cases
8. Kjør og rapporter faktiske resultater:
   ```bash
   ./mvnw test -pl <case-modul>
   ./mvnw verify -pl <case-modul>
   ./mvnw clean test-compile -DskipTests
   ```

**Ikke gjør uten separat eksplisitt tillatelse:** commit, push, PR, merge, eller
endring av `STATUS.md`. Fasitbranchen merges aldri tilbake til `main`.

Avslutt med sluttrapport: branch, endrede filer, implementerte regler, viktige
designvalg, testkommandoer og faktiske resultater, gjenstående usikkerheter.
