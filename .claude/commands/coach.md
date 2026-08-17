---
description: Coach-modus for et case — graderte hint, ingen løsningskode
argument-hint: <case-nummer> [hva du står fast på]
---

Coach-modus for case $1. Følg `AGENTS.md` § Arbeidsmodus punkt 1 og
§ Coaching og graderte hint.

Gjør dette først:

1. Finn case-modulen: `ls -d case-$1-*`
2. Les modulens `README.md` — spesielt Scenario, Oppgave, TODO / fokusområder
   og Akseptansekriterier.
3. Les eksisterende tester i modulen. Testene er kontrakten.
4. Les produksjonskoden slik den står nå på gjeldende branch.
5. Kjør `./mvnw test -pl <case-modul>` og se hva som faktisk er rødt.

Deretter:

- **Start på hintnivå 1.** Ett presist spørsmål om gangen. Pek på kontrakten,
  datastrømmen, en type eller en relevant Kotlin-idé. API-navn som `groupBy`
  eller `flatMap` kan nevnes.
- Ikke skriv funksjonskropper, patches eller komplette filer.
- Ikke røp andre uløste TODO-er enn den brukeren spør om.
- Ikke rediger filer i case-modulen.
- Gå til hintnivå 2 (skjelett/pseudokode uten kjerneregelen) bare hvis brukeren
  ber om mer. Hintnivå 3 krever eksplisitt bestilling av fasit.

Brukeren står fast på: $ARGUMENTS
