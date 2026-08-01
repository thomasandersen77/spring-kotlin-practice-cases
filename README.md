# Sopra Steria Kotlin/Spring Boot interview cases

Dette er et treningsrepositorium for teknisk intervju og parprogrammering i Kotlin, Spring Boot, JPA/Hibernate, DDD, SOLID og testbarhet. Repoet inneholder 30 små, uavhengige case-prosjekter — alle med vilje uferdige.

**Målet er ikke å pugge løsninger.** Målet er å kunne:
- oversette krav til tydelige domenevalg under tidspress
- skrive idiomatisk Kotlin — ikke "Java med Kotlin-syntaks"
- begrunne trade-offs muntlig, som i en intervju-debrief

## Slik jobber du iterativt med casene

Originaloppgavene ligger alltid på `main`. Du løser dem på egne branches, slik at du kan sammenligne med utgangspunktet og øve på nytt med friske øyne:

```bash
# Første forsøk på et case:
git checkout main
git checkout -b case-05-forsoek-1
# ... løs caset, kjør tester, tenk høyt ...
git add -A && git commit -m "case-05: første forsøk"

# Sammenlign med originaloppgaven:
git diff main...HEAD

# Oppdater STATUS.md med score og notater, og merge hvis du vil:
git checkout main && git merge case-05-forsoek-1

# Ny runde senere (andre/tredje gjennomkjøring — ny score):
git checkout -b case-05-forsoek-2 main
```

**Fremgang og score føres i [STATUS.md](STATUS.md).** Hvert forsøk scores 0.0–10.0 — poenget er å slå din egen forrige score når du husker og lærer mer.

## Øvingsform per case

1. Les casets `README.md` — spesielt scenario, TODO-er og akseptansekriterier.
2. Sett en tidsboks (se `## Tid` i casets README).
3. Kjør `mvn test -pl <modul>` og se hva som er rødt.
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

### Persistens, konsistens og samtidighet
| Case | Tema |
|---|---|
| `case-23-optimistic-locking-concurrency` | Versjonskontroll og konflikthåndtering ved samtidige reservasjoner |
| `case-24-domain-events-outbox` | Transactional outbox for konsistente domene-events |
| `case-25-idempotent-command-processing` | Idempotency keys for retry-sikker betalingshåndtering |
| `case-30-bank-account-crud-jpa` | Full stack: CRUD, JPA-relasjoner, transaksjoner, domeneoperasjoner |

### Sikkerhet
| Case | Tema |
|---|---|
| `case-28-oauth2-jwt-resource-server` | SecurityFilterChain, scopes og rollemapping fra custom claim |

## Anbefalt progresjon

| Runde | Caser | Fokus |
|---|---|---|
| 1 | 01, 02, 26 | Varm opp: ren Kotlin, TDD og idiomatisk syntaks |
| 2 | 03, 04, 05, 11, 12 | Domeneregler, value objects og enkle services |
| 3 | 13, 14, 16, 18, 10, 19, 15 | Aggregater, statusoverganger og presisjon |
| 4 | 06, 08, 09, 17, 07 | API, lagdeling og SOLID — avslutt med 07 som full Spring-flyt |
| 5 | 20, 21, 22, 29, 12 | Porter, adaptere og integrasjoner |
| 6 | 23, 24, 25, 27, 28 | Samtidighet, coroutines og sikkerhet |
| 7 | 30 | Full stack (JPA, transaksjoner, REST, domene) — "mesterprøven" |
| 8+ | Gjenta svake caser fra STATUS.md på nye branches | Slå din egen score |

## Kjøring

```bash
# Alle caser:
mvn test

# Ett case:
mvn test -pl case-01-pure-kotlin-domain

# Spring Boot-caser kan også startes:
mvn spring-boot:run -pl case-30-bank-account-crud-jpa
```

## Regler for repoet

- **Originaloppgaven på `main` skal alltid kompilere.** Røde tester er OK når de beskriver kontrakten kandidaten skal implementere — kompileringsfeil er ikke OK.
- **Ingen ferdige løsninger committes på `main`.** Løsninger lever på `case-NN-forsoek-M`-branches.
- **Hvert case følger samme README-mal:** Domene, Tid, Hva dette trener, Scenario, Oppgave, TODO / fokusområder, Akseptansekriterier, Formål i intervjuet, Ikke gjør det for lett, Intervjuspørsmål / debrief, Kommandoer.
- **Tester skal beskrive kontrakten**, ikke bare verifisere tall. Beskrivende backtick-navn på norsk/engelsk, JUnit 5 + AssertJ.
- Se [docs/TRENINGSGUIDE.md](docs/TRENINGSGUIDE.md) for detaljer.
