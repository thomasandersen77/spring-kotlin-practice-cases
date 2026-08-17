---
description: Start et nytt forsøk på et case — branch case-NN-forsoek-M fra ren main
argument-hint: <case-nummer> <forsøksnummer>
---

Start nytt forsøk på case $1, forsøk $2.

**Kontroller først at det er trygt å bytte branch:**

```bash
git status --short
git branch --show-current
```

Hvis working tree ikke er rent: stopp og spør brukeren hvordan de eksisterende
endringene skal bevares. Ikke bytt branch over ucommittede endringer.

Hent så oppdatert baseline og opprett branchen. `new-case-branch.sh` gjør dette
og bruker `git switch -C`, som **resetter branchen til main hvis den finnes fra
før**. Bekreft med brukeren før du kjører den hvis `case-$1-forsoek-$2` allerede
eksisterer.

```bash
git fetch origin
./new-case-branch.sh $1 $2
```

Etterpå:

1. Bekreft branch med `git branch --show-current`
2. Les case-modulens `README.md` og oppsummer kontrakten kort: domene, tidsboks,
   akseptansekriterier
3. Kjør `./mvnw test -pl <case-modul>` og vis hvilke tester som er røde
4. **Stopp der.** Ikke implementer noe. Coach-modus gjelder fra dette punktet,
   og brukeren skriver koden selv.

Minn om tidsboksen fra casets `## Tid` og at debrief hører med på slutten.
