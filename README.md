# Kotlin/Spring Boot Interview Cases

Dette er et treningsrepositorium for teknisk intervju og parprogrammering i Kotlin, Spring Boot, JPA/Hibernate, DDD, SOLID og testbarhet. Repoet inneholder 40 små, uavhengige case-prosjekter — alle med vilje uferdige.

**Målet er ikke å pugge løsninger.** Målet er å kunne:
- oversette krav til tydelige domenevalg under tidspress
- skrive idiomatisk Kotlin — ikke "Java med Kotlin-syntaks"
- begrunne trade-offs muntlig, som i en intervju-debrief

## Slik jobber du iterativt med casene

Originaloppgavene ligger alltid på `main`, og `main` skal alltid være den opprinnelige, uløste oppgaven. Løsningene dine lever på egne `case-NN-forsoek-M`-branches og merges **aldri** tilbake til `main`:

```bash
# Første forsøk på et case:
git switch main
git switch -c case-05-forsoek-1
# ... løs caset, kjør tester, tenk høyt ...
git add -A && git commit -m "case-05: første forsøk"

# Sammenlign forsøket med originaloppgaven:
git diff main...case-05-forsoek-1

# Tilbake til main og registrer KUN statusen (ingen merge av løsningen!):
git switch main
# ... rediger STATUS.md ...
git add STATUS.md && git commit -m "status: case-05 forsøk 1"

# Ny runde senere (andre/tredje gjennomkjøring — ny score, alltid fra ren main):
git switch -c case-05-forsoek-2 main
```

**Løsningsbranchen merges ikke til `main`.** Det er det som gjør at originaloppgaven alltid er intakt, og at du kan øve på nytt med friske øyne.

**Fremgang og score føres i [STATUS.md](STATUS.md).** Hvert forsøk scores 0.0–10.0 — poenget er å slå din egen forrige score når du husker og lærer mer.

## Øvingsform per case

1. Les casets `README.md` — spesielt scenario, TODO-er og akseptansekriterier.
2. Sett en tidsboks (se `## Tid` i casets README).
3. Kjør `./mvnw test -pl <modul>` og se hva som er rødt.
4. Skriv kontrakttester først der det er naturlig (TDD).
5. Fullfør TODO-ene stegvis — tenk høyt som i parprogrammering.
6. Avslutt med debrief: svar på casets `## Intervjuspørsmål / debrief` **høyt**, som om intervjueren sitter ved siden av deg.
7. Registrer score og notater i [STATUS.md](STATUS.md).

Se [docs/TRENINGSGUIDE.md](docs/TRENINGSGUIDE.md) for full treningsprotokoll, scoringsveiledning og regler for repoet.

## Casene, gruppert etter tema

### Ren Kotlin og syntaks
| Case | Tema |
|---|---|
| `case-01-pure-kotlin-domain` | Value classes, sealed classes, money-beregning uten Spring |
| `case-02-debug-and-test` | Debugging og kontrakttester: off-by-one-feil i kapasitetsberegner |
| `case-26-kotlin-idioms-drill` | Refaktorer Java-aktig Kotlin til idiomatisk Kotlin |
| `case-27-coroutines-structured-concurrency` | Coroutines: async/coroutineScope, timeout, kansellering, virtuell tid |
| `case-31-entity-dto-mapping` | Entitet → DTO med extension functions, list-mapping og delvis feltutvalg |
| `case-32-sealed-domain-modelling` | Sealed interface/`data object`, uttømmende `when` uten `else` |
| `case-33-collections-aggregation-report` | Collections-API i bredden: groupingBy, flatMap, partition, runningFold |
| `case-34-null-safety-validation` | Null-safety, `runCatching` og feilakkumulering med `ValidationResult` |
| `case-35-kotlin-dsl-higher-order-functions` | Lambda with receiver-DSL, `infix`/operatorer, `by lazy`, `buildString` |
| `case-37-kotlin-mapping-sealed-collections` | Mapping, sealed typer og sammensatt sortering |

### Domenemodellering / DDD
| Case | Tema |
|---|---|
| `case-03-business-rules-kata` | Forretningsregler for fakturaberegning (rabatter, VIP, koder) |
| `case-04-library-loan-domain` | Entity vs. Value Object, regler for forlengelse og forfalt lån |
| `case-05-parking-pricing-rules` | Domain Service for prising med avrunding og tidsintervaller |
| `case-10-shipping-slot-aggregate` | Kapasitet, value objects og invariants for leveringsbookinger |
| `case-11-hospital-triage-policy` | Domain Service som prioriterer pasienter |
| `case-13-warehouse-pick-list` | Aggregate med statusoverganger for plukklister |
| `case-14-flight-seat-booking` | Aggregate Root med konsistensgrense og domain event |
| `case-15-energy-tariff-billing` | Presise value objects og avrunding for strømavregning |
| `case-16-incident-escalation-state-machine` | Gyldige statusoverganger med historikk/audit |
| `case-18-ecommerce-cart-checkout` | Aggregate for handlekurv med transaksjons-/persistensgrense |
| `case-19-subscription-proration` | Dato- og money-beregning for midt-i-periode-oppgradering |

### API, lagdeling og SOLID
| Case | Tema |
|---|---|
| `case-06-restaurant-reservation-api` | Thin controller, validation og use case |
| `case-07-order-api-core-db` | API via core-lag til database, JPA som persistensdetalj |
| `case-08-refactor-fat-controller` | Refaktorer controller med blandede ansvarsområder |
| `case-09-current-user-rbac` | Testbar RBAC uten å blande inn Spring Security direkte |
| `case-17-feature-flag-rbac` | Access policy-regelmatrise for roller, miljø og produktområde |

### Integrasjon og porter/adaptere
| Case | Tema |
|---|---|
| `case-12-payment-settlement-strategy` | Strategy Pattern for gebyrberegning per betalingsmetode |
| `case-20-iot-sensor-alerting` | Ports/adapters og strategy for terskelbaserte alerts |
| `case-21-anti-corruption-layer` | Ekstern kredittintegrasjon oversatt til domenespråk |
| `case-22-insurance-claim-acl` | DTO-mapping og validering av ekstern skadedata |
| `case-29-llm-port-adapter-fallback` | LLM bak port/adapter: modell-fallback ved 503, feilsemantikk |
| `case-39-wiremock-external-integration` | RestClient, outbound port, ACL og WireMock |

### Persistens, konsistens og samtidighet
| Case | Tema |
|---|---|
| `case-23-optimistic-locking-concurrency` | Versjonskontroll og konflikthåndtering ved samtidige reservasjoner |
| `case-24-domain-events-outbox` | Transactional outbox for konsistente domene-events |
| `case-25-idempotent-command-processing` | Idempotency keys for retry-sikker betalingshåndtering |
| `case-30-bank-account-crud-jpa` | Full stack: CRUD, JPA-relasjoner, transaksjoner, domeneoperasjoner |
| `case-36-bank-transfer-fullstack-jpa` | Full stack: controller → service → domene → JPA → H2, atomisk overføring med rollback |
| `case-38-basic-spring-h2-rest` | REST-flyt med service, JPA, H2 og feiloversettelse |

### Sikkerhet
| Case | Tema |
|---|---|
| `case-28-oauth2-jwt-resource-server` | SecurityFilterChain, scopes og rollemapping fra custom claim |
| `case-40-security-request-context` | JWT, metodeautorisasjon, interceptor og trygg request-kontekst |

## Anbefalt progresjon

| Runde | Caser | Fokus |
|---|---|---|
| 1 | 01, 02, 26, 33 | Varm opp: ren Kotlin, TDD, idiomatisk syntaks og collections |
| 1b | 31, 32, 34, 35, 37 | Språkfeatures i dybden: mapping, sealed, null-safety og DSL — avslutt med 37 som Kotlin-konsolidering |
| 2 | 03, 04, 05, 11, 12 | Domeneregler, value objects og enkle services |
| 3 | 13, 14, 16, 18, 10, 19, 15 | Aggregater, statusoverganger og presisjon |
| 4 | 06, 08, 09, 17, 38, 07 | API, lagdeling og SOLID — bygg en grunnleggende Spring/H2-flyt i 38 før full Spring-flyt i 07 |
| 5 | 20, 21, 22, 39, 29, 12 (repetisjon i integrasjonskontekst) | Porter og ACL før ekte HTTP-integrasjon med WireMock, deretter fallback og repetisjon |
| 6 | 23, 24, 25, 27, 28 | Samtidighet, coroutines og sikkerhet |
| 7 | 30, 36 | Full stack (JPA, transaksjoner, REST, domene) — "mesterprøvene" |
| 8 | 40 | Avsluttende seniorcase: JWT, autorisasjon, request-livssyklus, ThreadLocal, H2 og audit |
| 9+ | Gjenta svake caser fra STATUS.md på nye branches | Slå din egen score |

## Kjøring

Bruk **Maven-wrapperen** (`./mvnw`, `.\mvnw.cmd` på Windows) — den henter Maven 3.9.6, så alle maskiner bygger med samme Maven-versjon. Du trenger ikke Maven installert lokalt.

**Java 21 er påkrevd.** Kotlin 1.9.25 støtter ikke JDK 22 eller nyere, og bygget feiler på JDK 25. Repoet har en `.sdkmanrc` — kjør `sdk env` i repo-roten (eller `sdk use java 21.0.7-tem`) før du bygger. Bygger du med feil JDK, stopper `maven-enforcer-plugin` med en tydelig feilmelding i stedet for en kryptisk Kotlin-krasj.

```bash
# Sjekk at wrapperen kjører på Java 21:
./mvnw -v

# Alle caser (rødt er forventet på main — TODO-ene er uløste):
./mvnw test

# Ett case:
./mvnw test -pl case-01-pure-kotlin-domain

# Verifiser at hele repoet kompilerer (uten å kjøre tester):
./mvnw clean test-compile -DskipTests

# Spring Boot-caser kan også startes:
./mvnw spring-boot:run -pl case-30-bank-account-crud-jpa
```

> `./mvnw test` på `main` gir **BUILD FAILURE** med vilje: casene er uferdige og kaster `NotImplementedError` fra `TODO()`. Bruk `./mvnw clean test-compile -DskipTests` når du vil sjekke at selve repoet er friskt — den skal alltid gi BUILD SUCCESS.

## Regler for repoet

- **Originaloppgaven på `main` skal alltid kompilere.** Røde tester er OK når de beskriver kontrakten kandidaten skal implementere — kompileringsfeil er ikke OK.
- **Ingen ferdige løsninger committes på `main`.** Løsninger lever på `case-NN-forsoek-M`-branches.
- **Hvert case følger samme README-mal:** Domene, Tid, Hva dette trener, Scenario, Oppgave, TODO / fokusområder, Akseptansekriterier, Formål i intervjuet, Ikke gjør det for lett, Intervjuspørsmål / debrief, Kommandoer.
- **Tester skal beskrive kontrakten**, ikke bare verifisere tall. Beskrivende backtick-navn på norsk/engelsk, JUnit 5 + AssertJ.
- Se [docs/TRENINGSGUIDE.md](docs/TRENINGSGUIDE.md) for detaljer.
