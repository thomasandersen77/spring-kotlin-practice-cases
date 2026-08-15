# Kotlin/Spring Boot Training Cases

Dette er et treningsrepositorium for teknisk trening og parprogrammering i Kotlin, Spring Boot, JPA/Hibernate, DDD, SOLID og testbarhet. Repoet inneholder 51 små, uavhengige case-prosjekter — alle med vilje uferdige.

**Målet er ikke å pugge løsninger.** Målet er å kunne:
- oversette krav til tydelige domenevalg under tidspress
- skrive idiomatisk Kotlin — ikke "Java med Kotlin-syntaks"
- begrunne trade-offs muntlig, som i en trenings-debrief

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
6. Avslutt med debrief: svar på casets `## Treningsspørsmål / debrief` **høyt**, som om coachen sitter ved siden av deg.
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

### Testing og TDD
| Case | Tema |
|---|---|
| `case-41-unit-testing-mocks-tdd` | Unit testing fra scratch med JUnit Jupiter, AssertJ, Mockito, MockK og TDD |
| `case-46-spring-security-mockmvc` | Security-testing med MockMvc, mock JWT og presis 200/201/401/403-kontrakt |

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
| `case-42-spring-mvc-validation` | Tynn Spring MVC-controller, Bean Validation, DTO-er og application service |
| `case-48-controller-advice-errors` | ControllerAdvice, stabil API-feilmodell og valideringsfeil |

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
| `case-47-transaction-boundary-jpa` | Liten atomisk arrangementsregistrering med rollback og unik constraint |

### Sikkerhet
| Case | Tema |
|---|---|
| `case-28-oauth2-jwt-resource-server` | SecurityFilterChain, scopes og rollemapping fra custom claim |
| `case-40-security-request-context` | JWT, metodeautorisasjon, interceptor og trygg request-kontekst |
| `case-43-current-user-argument-resolver` | Egen CurrentUser-annotasjon og argument resolver uten sikkerhetslekkasje til domenet |
| `case-44-jwt-claims-authorities` | Custom JWT claims mappet til scopes/roller med 401/403-tester |
| `case-45-method-security-authorization` | PreAuthorize og ressursbasert access policy for eierskap og roller |

### Kotlin web frameworks
| Case | Tema |
|---|---|
| `case-49-http4k-functional-http` | Funksjonell HTTP, lenses, filters og eksplisitt komposisjon med http4k |
| `case-50-ktor-rest-api` | Ktor routing, serialisering, suspending service og testApplication |
| `case-51-kotlin-web-framework-comparison` | Spring Boot vs. http4k vs. Ktor: pragmatiske trade-offs og teknologivalg |

## Anbefalt progresjon

| Runde | Caser | Fokus |
|---|---|---|
| 1 | 01, 02, 41, 26, 33 | Varm opp: ren Kotlin, unit testing/TDD, idiomatisk syntaks og collections |
| 1b | 31, 32, 34, 35, 37 | Språkfeatures i dybden: mapping, sealed, null-safety og DSL — avslutt med 37 som Kotlin-konsolidering |
| 2 | 03, 04, 05, 11, 12 | Domeneregler, value objects og enkle services |
| 3 | 13, 14, 16, 18, 10, 19, 15 | Aggregater, statusoverganger og presisjon |
| 4 | 06, 08, 42, 48, 09, 17, 38, 07 | API, Bean Validation, feilmodell, lagdeling og SOLID — bygg deretter en grunnleggende Spring/H2-flyt i 38 før full Spring-flyt i 07 |
| 5 | 20, 21, 22, 39, 29, 12 (repetisjon i integrasjonskontekst) | Porter og ACL før ekte HTTP-integrasjon med WireMock, deretter fallback og repetisjon |
| 6 | 28, 43, 44, 45, 46 | Security-grunnlag → elegant current user → JWT-authorities → method security → målrettet MockMvc-testing |
| 7 | 23, 24, 25, 27 | Konsistens, samtidighet og coroutines |
| 8 | 47, 30, 36 | Transaksjonsgrense i liten skala før full stack (JPA, REST og domene) |
| 9 | 49, 50 | http4k og Ktor som Kotlin-native variasjon etter Spring-grunnlaget |
| 10 | 40, 51 | Seniorkonsolidering: request-livssyklus, audit og begrunnet valg mellom Spring/http4k/Ktor |
| 11+ | Gjenta svake caser fra STATUS.md på nye branches | Slå din egen score |

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

- **Originaloppgaven på `main` skal alltid kompilere.** Røde tester er OK når de beskriver kontrakten deltakeren skal implementere — kompileringsfeil er ikke OK.
- **Ingen ferdige løsninger committes på `main`.** Løsninger lever på `case-NN-forsoek-M`-branches.
- **Hvert case følger samme README-mal:** Domene, Tid, Hva dette trener, Scenario, Oppgave, TODO / fokusområder, Akseptansekriterier, Formål i treningen, Ikke gjør det for lett, Treningsspørsmål / debrief, Kommandoer.
- **Tester skal beskrive kontrakten**, ikke bare verifisere tall. Beskrivende backtick-navn på norsk/engelsk, JUnit 5 + AssertJ.
- Se [docs/TRENINGSGUIDE.md](docs/TRENINGSGUIDE.md) for detaljer.
