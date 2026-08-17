---
description: Sammenlign et forsøk mot oppgaven på main og eventuell fasit (SAMMENLIGN)
argument-hint: <case-nummer> [forsøksnummer]
---

Kjør `SAMMENLIGN` fra `docs/AGENTS-WORKFLOW.md` for case $1.

Les kontrakten og testene **før** du vurderer diffen:

1. Finn case-modulen: `ls -d case-$1-*`
2. Les modulens `README.md` og eksisterende tester
3. `git branch -a` for å se hvilke forsøks- og fasitbrancher som finnes

Sammenlign først forsøket mot oppgaven på `main`:

```bash
git diff main...case-$1-forsoek-<M> -- <case-modul>
```

Deretter, hvis `case-$1-fasit` finnes, forsøk mot fasit:

```bash
git diff case-$1-forsoek-<M>..case-$1-fasit -- <case-modul>
```

Avgrens alltid til case-modulen, slik at urelaterte endringer på `main` ikke
trekkes inn i vurderingen.

Rapporter i denne rekkefølgen:

1. **Observasjoner og spørsmål** — hva er faktisk forskjellig, og hvorfor tror
   du forsøket valgte sin variant?
2. Deretter eventuelle hint.

Skill tydelig mellom:

- korrekthetsfeil
- kontraktsbrudd mot akseptansekriteriene
- Kotlin-idiomatikk og lesbarhet
- designvalg der begge varianter er forsvarlige
- produksjonsforbedringer utenfor casets tidsboks

**Ikke kopier fasit inn i forsøket.** Ikke endre filer. En fasit som er
annerledes er ikke automatisk bedre — si det hvis forsøket har det bedre valget.
