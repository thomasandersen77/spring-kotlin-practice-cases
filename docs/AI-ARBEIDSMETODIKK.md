# AI-arbeidsmetodikk for Kotlin-ferdighetstrening

Dette dokumentet beskriver anbefalt bruk av AI-verktøy i repositoryet
`spring-kotlin-practice-cases`. Målet er maksimal egen læring og
treningsberedskap i Kotlin, Spring Boot og Java under tidspress — ikke
maksimal AI-automasjon.

Dokumentet er personlig metodikk. Ved motstrid gjelder `AGENTS.md` og nærmeste
case-kontrakt foran denne filen.

## 1. Grunnprinsipp

```text
 THOMAS
 │
 IntelliJ IDEA
 │
 skriver og løser selv
 │
 ┌────────────┴────────────┐
 │ │
 ChatGPT + GitHub Warp
 coach / review / score lokal Git/test/diff
 debrief / trening worktree/kontroll
 │
 │ eksplisitt behov for
 │ agentisk utførelse/fasit
 ▼
 Codex ELLER Claude Code
 sidestilt — én utfører per jobb

JetBrains AI = selektiv inline-hjelp
```

Kort sagt:

> **Thomas tenker og skriver i IntelliJ. 
> ChatGPT + GitHub er faglig coach/reviewer. 
> Warp er lokal sannhetskontroll. 
> Codex og Claude Code er sidestilte agentiske utførere ved eksplisitt 
> bestilling — velg ett av dem per jobb. 
> JetBrains AI er selektiv inline-hjelp.**

Du trener på å:

- forstå kontrakten og avgrense problemet
- implementere og teste selv
- begrunne designvalg og trade-offs
- forklare løsningen presist i en teknisk treningssituasjon

AI skal støtte denne loopen, ikke erstatte den.

### 1.1 Trening vs produksjonsarbeid

Optimal bruk av AI i produksjon og optimal bruk av AI i ferdighetstrening er to
forskjellige problemer.

**I ekte kunde-/produksjonsarbeid** kan AI brukes aggressivt når det øker
kvalitet, sparer tid, reduserer feil, forbedrer dokumentasjon eller
effektiviserer analyse.

**I ferdighetstreningen** brukes AI mer restriktivt fordi målet er:

- egen problemløsning
- Kotlin-/Java-/Spring-retrieval
- testing og debugging
- designresonnement
- muntlig forklaring

Dette dokumentet gjelder **ferdighetstreningen**.

## 2. Verktøykart

| Verktøy | Primær rolle | Brukes til | Brukes ikke til |
|---|---|---|---|
| **Thomas + IntelliJ** | Implementasjon og problemløsning | Forstå kontrakt, skrive case-kode, feilsøke, refaktorere, forklare | Å overlate hele caset til AI |
| **ChatGPT + GitHub** | Coach, review, score, debrief | Hint, vurdering, scoring, treningssimulering, kontinuitet mellom økter | Lokal git-sannhet for upush-ede endringer; uoppfordret fasit |
| **Warp** | Lokal kontrollør og terminalpartner | `git status`, branch/worktree, lokal diff, tester, faktiske testfeil, Git-sikkerhet | Primær scorer; uoppfordret fasit; erstatning for egen koding |
| **Codex** | Agentisk utfører ved bestilling | `FASIT_CODEX`, `case-NN-fasit`, flerfilsjobber, docs, eksplisitte GitHub-leveranser | Første forsøk på uløste TODO-er; små syntaxproblemer; «treningsløsning» |
| **Claude Code** | Agentisk utfører ved bestilling, sidestilt med Codex | Samme jobber som Codex; i tillegg coach/review lokalt via slash-kommandoene i `.claude/commands/` | Samme grenser som Codex; og aldri parallelt med Codex på samme filsett |
| **JetBrains AI** | Selektiv inline editorhjelp | Autocomplete, imports, API-hint, rename/extract, syntax, små forklaringer | Full funksjonsgenerering, hele testklasser, TODO-løsning, multi-file-agent |
| **Warp Agent CLI** | CLI uten Warp-app | Server/SSH/CI uten GUI | Normal lokal trening (Warp-appen er nok) |

### 2.1 Hovedroller i klartekst

#### Thomas + IntelliJ

Primær arena for implementasjon. Thomas skal selv:

- forstå kontrakten
- skrive case-koden
- feilsøke
- refaktorere
- forklare løsningen

#### ChatGPT + GitHub

Primær **coach, reviewer, scorer og fasilitator** for prosjektet.

Begrunnelse:

- ChatGPT har prosjektkontekst og treningshistorikk
- ChatGPT kan lese push-ede branches på GitHub
- README, kode og repo-regler kan vurderes samlet
- dette gir kontinuitet mellom caseøktene

Viktig begrensning:

> ChatGPT/GitHub ser repository-tilstanden som finnes på GitHub, men ikke
> automatisk lokale, upush-ede endringer.

Når noe bare finnes lokalt, må Warp levere evidens (diff, status, tester) inn i
reviewen.

#### Warp

Primær rolle: **lokal kontrollør, terminalpartner og sannhetskilde for lokal
repo-tilstand**.

Warp brukes primært til:

- `git status`
- branch-kontroll
- worktree-kontroll
- lokal diff
- staged/unstaged/untracked endringer
- Maven/testkjøring
- analyse av faktiske lokale testfeil
- Git-sikkerhet (`KONTROLL_WARP`)
- rask lokal kodebaseanalyse når endringen ikke er pushet

Warp kan fortsatt brukes som coach/debrief-verktøy, men det er **sekundært**.

Prinsipp:

> ChatGPT = faglig coach/reviewer 
> Warp = lokal sannhetskontroll

#### Codex og Claude Code

**Sidestilte agentiske utførere.** Begge brukes når AI faktisk skal
implementere noe etter eksplisitt bestilling. Velg fritt ut fra hva som passer
jobben, hvilket verktøy du sitter i, og hva du vil lære å bruke godt.

De følger samme regelverk (`AGENTS.md`), samme arbeidsflyter
(`docs/AGENTS-WORKFLOW.md`) og samme branchmodell. Ingen av dem har utvidede
tillatelser.

Begge brukes primært til:

- eksplisitt bestilt fasit
- `case-NN-fasit` på egen branch
- større flerfilsjobber
- mekaniske repo-endringer
- dokumentasjonsarbeid
- eksplisitt bestilte GitHub-leveranser

Ingen av dem brukes normalt til:

- første forsøk på uløste TODO-er
- små syntaxproblemer
- å skrive den første testen
- å løse case mens Thomas trener

Forsøk og fasit holdes alltid på separate brancher.

**Praktisk forskjell mellom de to:**

- **Codex** har prosjektkontekst fra ChatGPT-prosjektet og
  `.codex/environments/environment.toml` med ferdige actions som finner riktig
  modul ut fra branchnavnet.
- **Claude Code** kjører lokalt mot working tree og ser derfor ucommittede
  endringer direkte. Slash-kommandoene i `.claude/commands/` dekker coach,
  kontroll, review, sammenligning, nytt forsøk og fasit.

Velg ut fra det, ikke ut fra rang.

> Én agentisk utfører per jobb. Ikke kjør Codex og Claude Code på samme filsett
> samtidig — ikke fordi det ene er bedre, men fordi to agenter som endrer samme
> branch gir uklar historikk og uklart eierskap.

#### JetBrains AI

Selektiv inline-hjelp — ikke fri agent.

**Under normal trening** kan JetBrains AI brukes til:

- autocomplete
- imports
- API-hint
- rename/extract
- syntax
- små lokale forklaringer

Unngå:

- full funksjonsgenerering
- hele testklasser
- løsning av TODO-er
- agentiske multi-file-endringer

**Under realistisk treningssimulering** skal AI-hjelp kunne slås helt av:

- JetBrains AI av
- ChatGPT av
- Warp-agent av
- Codex av
- Claude av

Tillatt da:

- IntelliJ
- terminal
- compiler
- tester
- debugger
- eventuell dokumentasjon dersom treningsformatet tillater det

Poenget er å teste egen retrieval og problemløsning.

### 2.2 Warp-app vs Warp Agent CLI

- **Warp-appen** er den fulle GUI-terminalen med innebygd agent. Dette er
 primærflaten for lokal kontroll.
- **Warp Agent CLI** er et lett CLI for agentsamtaler uten appen. Gir liten
 merverdi når Warp allerede er favorittterminalen.

## 3. Én primær scorer — ingen score-shopping

**ChatGPT + GitHub = primær reviewer/scorer.**

Ikke be Warp, ChatGPT, Claude og Codex om å score samme løsning bare for å få
flere tall.

Warp kan levere lokal evidens til reviewen:

- diff
- testresultater
- branchstatus
- working tree

Second opinion brukes bare når det finnes en **konkret faglig grunn**, f.eks.
motstrid mellom score og tester, uklar kontraktstolkning, eller behov for en
avgrenset second look på ett designvalg.

Repo-bevis (kode, tester, faktisk diff, kjørte resultater) slår chatutdrag og
gamle scorer.

## 4. Kreditter og modellvalg

### 4.1 Warp

Bruk Warp mest til lokal kontroll. Da holder ofte billig/auto-modell.

| Situasjon | Anbefalt modell |
|---|---|
| Daglig default | `auto (cost-efficient)` eller tilsvarende billig/auto |
| Git/status/diff/test/feilanalyse | Billig / cost-efficient |
| Sekundær coach/debrief lokalt | Sterkere modell midlertidig ved behov |
| Hele dagen på dyr modell | Unngås |

Rekkefølge for kredittforbruk i Warp:

1. Planens base credits
2. Personal / add-on credits
3. Eventuelt kjøp eller auto-reload ved behov

### 4.2 ChatGPT + GitHub

Bruk den modellen/arbeidsflaten som gir best kontinuitet for coach, review,
score og debrief i dette prosjektet. Hold review basert på push-et underlag +
eventuell Warp-evidens for lokale endringer.

### 4.3 Codex

Fullverdig valg når noe skal **utføres** agentisk etter eksplisitt bestilling,
inkludert fasit via `FASIT_CODEX`.

### 4.4 Anthropic / Claude Code

Fullverdig valg på lik linje med Codex. Sterkest når arbeidet er lokalt: du har
ucommittede endringer, du vil kjøre `./mvnw` og lese faktiske testfeil, eller du
vil bruke slash-kommandoene til coach/review i samme flate som koden.

| Modellklasse | Når |
|---|---|
| **Claude Sonnet** | Default for kode, review og analyse |
| **Claude Opus** | Dyp arkitektur og vanskelige trade-offs der det trengs |
| **Claude Haiku** | Sjelden relevant i denne treningen |

### 4.5 Én utfører

Kjør **ikke** samme implementasjonsjobb samtidig i Codex + Claude Code (+ Warp
som endringsagent).

Flere verktøy kan *lese/vurdere* og levere evidens. Bare **én** agentisk utfører
skal *endre* per jobb, med mindre du bevisst skiller worktrees og roller.

## 5. Roller mot repository-reglene

Dette repositoryet har egne agentregler (`AGENTS.md`, `docs/AGENTS-WORKFLOW.md`,
`docs/TRENINGSGUIDE.md`). Metodikken under forutsetter dem. Denne filen endrer
ikke de offisielle reglene.

### 5.1 Modus

| Modus | Når | AI-adferd |
|---|---|---|
| **Coach** (default) | Trening, spørsmål, uløste TODO-er | Hint nivå 1–2, ingen kopierbar fasit. Primært ChatGPT; Warp sekundært |
| **Review** | «vurder», «score», «review» | Primært ChatGPT + GitHub. Warp leverer lokal evidens. Følg `docs/TRENINGSGUIDE.md`. Ikke endre filer |
| **Utførelse** | Eksplisitt bestilling om endring | Codex eller Claude Code. Begrenset omfang, bevar urelatert arbeid |
| **Fasit** | Eksplisitt `FASIT_CODEX` / «gi meg fasit» | Codex eller Claude Code (`/fasit`), `case-NN-fasit` fra `origin/main` |

### 5.2 Git- og worktree-sikkerhet

- Forsøk: `case-NN-forsoek-M`
- Fasit: `case-NN-fasit`
- IntelliJ-forsøk og fasit-agent skal bruke **separate worktrees**
- Ikke implementer case-løsning direkte på `main`
- Ikke merge forsøk/fasit tilbake til `main` som del av treningen
- Commit / push / PR / merge krever **separate eksplisitte tillatelser**
- Warp brukes som standard som rent lesende kontrollør via `KONTROLL_WARP`

Før branch, commit, push eller sammenligning:

```bash
git status --short
git branch --show-current
git worktree list
git fetch origin
```

Ved review av forsøk, vurder mot branchens faktiske utgangspunkt:

```bash
BASE=$(git merge-base origin/main HEAD)
git diff "$BASE"..HEAD
git status --short
git diff
git diff --cached
```

### 5.3 Hintnivå (coach)

1. **Nivå 1:** ett presist spørsmål, retning, evt. API-navn — ingen løsningskode
2. **Nivå 2:** skjelett/pseudokode der kjerneregelen mangler
3. **Nivå 3:** fasit/komplett løsning — bare ved eksplisitt bestilling

## 6. Maks AI-avbrudd under implementasjon

Dette er en anbefalt standardregel, ikke bare et senere vedtak.

For et typisk 45–60 minutters case:

### Første 15 minutter

Ingen AI-hjelp.

Thomas skal selv:

- lese kontrakten
- forstå modellene
- starte løsningen
- lese compiler-/testfeil

### Deretter (implementasjonsfasen)

Maks **2–3 AI-avbrudd**.

- Start alltid med hintnivå 1
- Foretrekk ChatGPT for faglige hint
- Bruk Warp hvis spørsmålet egentlig er lokal tilstand, testfeil eller git
- JetBrains AI-autocomplete teller ikke som eget «avbrudd», men full
 funksjonsgenerering gjør det — og skal unngås

### Først etter tidsboksen

Tillat:

- dypere review
- større hint
- sammenligning
- eventuell fasit

Målet er ikke å gjøre utvikling kunstig vanskelig, men å bygge evnen til å
løse problemer uten agentisk støtte i treningen.

## 7. Anbefalt øktmal (1–2 timer)

### Steg 1 — Start (5–15 min) · IntelliJ + eventuelt Warp

- Velg case og mål for økten
- Les case-README og relevante tester i IntelliJ
- Bruk Warp ved behov for branch/worktree/`git status`
- Formuler kontrakten med egne ord før du koder
- Første del av implementasjonen skjer uten AI (se §6)

### Steg 2 — Implementer (45–90 min) · Thomas + IntelliJ

- Du skriver produksjonskoden
- JetBrains AI bare selektivt (autocomplete/API/syntax)
- Maks 2–3 eksterne AI-hint; start på nivå 1
- ChatGPT som coach ved behov
- Warp bare når du trenger lokal feil-/git-sannhet
- Ikke be om full funksjonskropp før du har forsøkt selv

### Steg 3 — Lokal verifisering (10–20 min) · terminal / Warp

```bash
./mvnw test -pl <case-modul>
```

Ved behov:

```bash
./mvnw verify -pl <case-modul>
```

Bruk også:

```bash
git status --short
git diff
```

Be Warp om analyse av faktiske lokale testfeil og diff — ikke stille patch med
mindre du eksplisitt bytter til utførelsesmodus og velger en utfører.

### Steg 4 — Review / score · ChatGPT + GitHub

Når forsøket er klart nok:

1. Push branch når du selv ønsker at ChatGPT skal se samme tilstand som GitHub
2. Hvis det finnes viktige upush-ede endringer: hent Warp-evidens først
3. Be ChatGPT om review/score etter `docs/TRENINGSGUIDE.md`

```text
Review-modus for case <NN> på branch <branch>.
Les README, tester og relevant diff på GitHub.
Bruk eventuell vedlagt Warp-evidens for lokale/upush-ede endringer.
Kjør eller legg til grunn faktiske testresultater når de finnes.
Skill bug, kontraktsbrudd, designvalg og idiomatikk.
Følg docs/TRENINGSGUIDE.md.
Ikke endre filer.
Én primær score — ikke score-shopping.
```

### Steg 5 — Debrief (10–15 min) · ChatGPT som seniorcoach

Øv på å forklare uten å lese opp kode:

- kontrakt og avgrensning
- invariants og edge cases
- Kotlin-datastrøm / viktige API-valg
- arkitektur og designvalg
- teststrategi
- trade-offs og hva som ville vært annerledes i produksjon

Warp kan brukes sekundært til lokal debrief hvis GitHub-tilstand ikke er
oppdatert, men ChatGPT er primær coach.

### Steg 6 — Fasit (sjeldent) · Codex eller Claude Code

Bare når du eksplisitt vil ha referanseløsning:

- følg `FASIT_CODEX` (i Claude Code: `/fasit <NN>`)
- branch `case-NN-fasit` fra verifisert `origin/main`
- ikke merge til `main`
- etterpå: sammenlign forsøk vs fasit (`SAMMENLIGN` / `/sammenlign <NN>`),
 gjerne med ChatGPT på push-et underlag og Warp eller Claude Code på lokal diff

Velg verktøy fritt, men bruk bare ett per fasitjobb.

## 8. Hvem eier hvilken jobb?

| Jobb | Primær | Sekundær / evidens | Kommentar |
|---|---|---|---|
| Forstå case-kontrakt | Thomas | ChatGPT coach | Les README + tester først |
| Skrive case-kode | Thomas i IntelliJ | Selektiv JetBrains AI | Ingen uoppfordret full løsning |
| Rask editorhjelp | JetBrains AI | — | Bare inline; ikke multi-file-agent |
| Hint ved fastlåsning | ChatGPT | Warp lokalt | Nivå 1 først; maks 2–3 avbrudd |
| Kjøre tester / tolke lokale feil | Thomas + Warp | — | Rapporter faktiske resultat |
| Git / worktree / branch-kontroll | Warp (`KONTROLL_WARP`) | Thomas | Eksplisitt tillatelse før endring |
| Review og score | ChatGPT + GitHub | Warp-evidens | Én primær scorer |
| Muntlig debrief | ChatGPT | Warp sekundært | Treningssimulering |
| Full fasit | Codex eller Claude Code (`FASIT_CODEX`) | — | Etter eksplisitt bestilling; ett verktøy per jobb |
| PR / GitHub-leveranse | Codex (eller manuell `gh`) | — | Egne tillatelser |
| Oppdatere `STATUS.md` | Thomas, evt. agent på bestilling | — | Commit/push er separate steg |

## 9. Forslag til ukerytme

| Dagtype | Fokus | Verktøy |
|---|---|---|
| 4–5 økter | Nytt forsøk eller forbedring i coach-modus | Thomas + IntelliJ; begrenset JetBrains AI; ChatGPT-hint; Warp for lokal kontroll |
| 1 økt | Review, score, debrief | ChatGPT + GitHub; Warp-evidens ved behov |
| 0–1 økt | Fasit + sammenligning | Codex eller Claude Code; deretter ChatGPT/Warp for sammenligning |
| Periodisk | Realistisk treningssimulering uten AI | Bare IntelliJ + terminal/tester/debugger |
| Løpende | STATUS og progresjon | Manuelt eller bestilt oppdatering |

### Prioritering ut fra progresjon

- **Høy score, påbegynt:** bruk AI til å presse forklaring og edge cases, ikke
 til å kosmetisk jage poeng eller shoppe ny score.
- **Lav score / tidlig forsøk:** mer egen implementasjon + coach-hint; fasit
 først etter nytt reelt forsøk.
- **Ikke startet:** start med egen kontraktavgrensning i IntelliJ; ChatGPT bare
 etter første eget forsøk på å formulere problemet.

## 10. Prompter du kan gjenbruke

### 10.1 Coach (ChatGPT)

```text
Coach-modus for case <NN>.
Ikke skriv fasitkode eller full funksjonskropp.
Gi hint nivå 1.
Jeg sitter fast på: <kort beskrivelse>.
Jeg har allerede forsøkt: <kort>.
```

### 10.2 Lokal kontroll (Warp / KONTROLL_WARP)

```text
KONTROLL_WARP for case <NN>.
Lesende kontroll bare.
Rapporter branch, worktree, status, relevant diff og testresultater.
Ikke rediger, commit, push eller bytt branch.
```

### 10.3 Review / score (ChatGPT + GitHub)

```text
Review-modus for case <NN> på branch <branch>.
Les README, tester og diff på GitHub.
Bruk vedlagt Warp-evidens hvis noe er lokalt/upushet.
Følg docs/TRENINGSGUIDE.md.
Skill bug, kontraktsbrudd, designvalg og idiomatikk.
Gi én primær score. Ikke score-shopping.
Ikke endre filer.
```

### 10.4 Debrief (ChatGPT)

```text
Trenings-debrief for case <NN>.
Still oppfølgingsspørsmål som en seniorcoach.
Ikke godta upresise begreper.
Hjelp meg å formulere korte, korrekte svar uten å overta resonnementet.
```

### 10.5 Fasit (Codex eller Claude Code)

```text
Fasitmodus for case <NN>.
Følg FASIT_CODEX / repo-reglene.
Branch case-<NN>-fasit fra origin/main i samme checkout.
Endre bare case-modulen.
Ikke merge til main. Ikke PR med mindre jeg ber om det separat.
```

I Claude Code er dette `/fasit <NN>`.

### 10.6 Sammenligning

```text
SAMMENLIGN case-<NN>-forsoek-M med case-<NN>-fasit.
Les kontrakt og tester først.
Ikke kopier fasit inn i forsøket.
Skill korrekthetsfeil, kontraktsbrudd, idiomatikk, designvalg og
produksjonsforbedringer utenfor tidsboksen.
```

### 10.7 Second opinion (bare ved konkret faglig grunn)

```text
Second opinion på ett avgrenset spørsmål: <spørsmål>.
Primær review er allerede gjort av ChatGPT.
Ikke gi ny totalscore med mindre jeg eksplisitt ber om re-score av god grunn.
```

### 10.8 Slash-kommandoer i Claude Code

Prompene over er kodet som slash-kommandoer i `.claude/commands/`, slik at de
er identiske hver gang og følger `AGENTS.md` uten at du må gjenta reglene:

| Kommando | Tilsvarer |
|---|---|
| `/coach <NN>` | 10.1 Coach |
| `/kontroll [NN]` | 10.2 Lokal kontroll (`KONTROLL_WARP`) |
| `/review <NN>` | 10.3 Review / score |
| `/sammenlign <NN>` | 10.6 Sammenligning (`SAMMENLIGN`) |
| `/nytt-forsoek <NN> <M>` | Ny forsøksbranch fra ren `main` |
| `/fasit <NN>` | 10.5 Fasit (`FASIT_CODEX`) |

Kommandoene er committet og gjelder for alle som sjekker ut repoet.

## 11. Hva «best bruk av Warp» betyr i praksis

Warp er sterkest som:

1. **Lokal sannhetskontroll** for branch/worktree/diff/status
2. **Terminalpartner** for Maven/test og faktiske feil
3. **Git-sikkerhetsnett** via `KONTROLL_WARP`
4. **Sekundær** coach/debrief når noe ikke er pushet eller trengs lokalt raskt

Warp er svakest brukt som:

- primær scorer i konkurranse med ChatGPT
- autopilot som løser alle TODO-er
- erstatning for IntelliJ ved linje-for-linje koding hele dagen
- dyr modell stående på default for små git-spørsmål
- parallell «tredje koder» ved siden av Codex eller Claude Code på samme filsett

## 12. Sjekkliste før du lukker en økt

- [ ] Jeg kan forklare kontrakten uten å lese README
- [ ] Jeg vet hvilke tester som er grønne/røde fordi jeg kjørte dem
- [ ] AI skrev ikke løsningen for meg uten at jeg ba om fasit
- [ ] Første del av økten hadde reell egen problemløsning uten AI
- [ ] Jeg holdt meg til maks 2–3 AI-avbrudd i implementasjonsfasen (eller noterte avvik)
- [ ] Riktig branch/worktree er brukt
- [ ] Eventuell review har én primær scorer (ChatGPT + GitHub)
- [ ] Upush-ede endringer er enten pushet eller dokumentert via Warp-evidens
- [ ] Neste økt har ett tydelig mål
- [ ] STATUS oppdateres bare bevisst (og commit/push bare med tillatelse)

## 13. Vedtak låst i denne metodikken

1. **Default i Warp:** billig/auto (`cost-efficient`) for lokal kontroll
2. **Sterk modell i Warp:** bare ved behov for lokal analyse/sekundær debrief
3. **Fasit/agentisk utførelse:** **Codex og Claude Code er sidestilte** — velg
 fritt, men bruk bare ett verktøy per jobb
4. **Maks AI-avbrudd:** 2–3 i implementasjonsfasen etter 15 min uten AI
5. **Debrief:** ja etter forsøk som skal scores eller lukkes
6. **Denne filen:** personlig metodikk; refereres ikke automatisk fra `AGENTS.md`
7. **Primær scorer:** ChatGPT + GitHub
8. **Én agentisk utfører per jobb**

## 14. Relaterte filer i repoet

- `AGENTS.md` — overordnede agentregler (autoritative ved konflikt)
- `CLAUDE.md` — Claude Code-oppsett; importerer `AGENTS.md`, endrer ikke reglene
- `.claude/settings.json` — delte tillatelser for Claude Code (committet)
- `.claude/commands/` — slash-kommandoer for coach, kontroll, review,
 sammenligning, nytt forsøk og fasit
- `.codex/environments/environment.toml` — Codex-oppsett og actions (autogenerert)
- `docs/AGENTS-WORKFLOW.md` — `FASIT_CODEX`, `KONTROLL_WARP`, `SAMMENLIGN`
- `docs/TRENINGSGUIDE.md` — review og scoring
- `STATUS.md` — committet progresjon

---

*Personlig metodikk for ferdighetstrening. Ikke fasit for repoets offisielle
regler. Ved motstrid gjelder `AGENTS.md` og nærmeste case-kontrakt foran denne
filen.*
