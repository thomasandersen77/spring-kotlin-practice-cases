# Status og score per case

Her føres fremgangen din per case. Oppdater raden etter hvert forsøk — og registrer hvert nye forsøk (ny branch) i historikken under.

**Statusverdier:** `Ikke startet` · `Påbegynt` · `Løst` · `Mestret`

- **Påbegynt** = forsøk er gjort på branch, men caset er ikke ferdig (røde tester, uløste TODO-er, eller tidsboks stoppet arbeidet). Delscore og notater skal likevel føres.
- **Løst** = testene er grønne og akseptansekriteriene er møtt.
- **Mestret** = løst minst to ganger på separate branches, med lik eller bedre score andre gang — du kan caset, ikke bare husker løsningen.

Ufullstendige forsøk er førsteklasses data: registrer dem. En ærlig 4.5 på `Påbegynt` er mer verdifull enn å vente til alt er grønt.

## Scoringskala (0.0–10.0)

| Score | Betydning |
|---|---|
| 0.0–2.0 | Kom i gang, men stoppet tidlig. Mye igjen av kjernen. |
| 3.0–4.0 | Delvis løst. Kjernelogikk på plass, men tester/design henger ikke sammen. |
| 5.0–6.0 | Fungerer, men ufullstendig testdekning eller uklare designvalg. |
| 7.0–8.0 | Løst med god testdekning. Kan forklare de fleste valgene. |
| 9.0–10.0 | Løst, testet og begrunnet. Debrief-spørsmålene besvares flytende. Trade-offs beherskes. |

Tips til score: trekk fra for manglende edge-tester, for valg du ikke kan begrunne muntlig, og for tidsoverskridelse. Legg til for idiomatisk Kotlin og tydelig domenespråk.

## Oversikt

| Case | Tema | Status | Beste score | Siste forsøk | Branch |
|---|---|---|---|---|---|
| 01 pure-kotlin-domain | Kotlin / DDD | Løst | 7.5 | 2026-07-27 | main |
| 02 debug-and-test | Kotlin / TDD | Løst | 8.9 | 2026-08-01 | case-02-forsoek-1 |
| 03 business-rules-kata | DDD | Ikke startet | – | – | – |
| 04 library-loan-domain | DDD | Ikke startet | – | – | – |
| 05 parking-pricing-rules | DDD | Ikke startet | – | – | – |
| 06 restaurant-reservation-api | API / SOLID | Ikke startet | – | – | – |
| 07 order-api-core-db | API / JPA | Ikke startet | – | – | – |
| 08 refactor-fat-controller | SOLID | Ikke startet | – | – | – |
| 09 current-user-rbac | API / SOLID | Ikke startet | – | – | – |
| 10 shipping-slot-aggregate | DDD | Ikke startet | – | – | – |
| 11 hospital-triage-policy | DDD | Ikke startet | – | – | – |
| 12 payment-settlement-strategy | Integrasjon | Ikke startet | – | – | – |
| 13 warehouse-pick-list | DDD | Ikke startet | – | – | – |
| 14 flight-seat-booking | DDD | Ikke startet | – | – | – |
| 15 energy-tariff-billing | DDD | Ikke startet | – | – | – |
| 16 incident-escalation-state-machine | DDD | Ikke startet | – | – | – |
| 17 feature-flag-rbac | API / SOLID | Ikke startet | – | – | – |
| 18 ecommerce-cart-checkout | DDD | Ikke startet | – | – | – |
| 19 subscription-proration | DDD | Ikke startet | – | – | – |
| 20 iot-sensor-alerting | Integrasjon | Ikke startet | – | – | – |
| 21 anti-corruption-layer | Integrasjon | Ikke startet | – | – | – |
| 22 insurance-claim-acl | Integrasjon | Ikke startet | – | – | – |
| 23 optimistic-locking-concurrency | Samtidighet | Ikke startet | – | – | – |
| 24 domain-events-outbox | Samtidighet | Ikke startet | – | – | – |
| 25 idempotent-command-processing | Samtidighet | Ikke startet | – | – | – |
| 26 kotlin-idioms-drill | Kotlin | Løst | 9.2 | 2026-08-04 | case-26-forsoek-1 |
| 27 coroutines-structured-concurrency | Kotlin / Samtidighet | Ikke startet | – | – | – |
| 28 oauth2-jwt-resource-server | Sikkerhet | Ikke startet | – | – | – |
| 29 llm-port-adapter-fallback | Integrasjon | Ikke startet | – | – | – |
| 30 bank-account-crud-jpa | Full stack / JPA | Ikke startet | – | – | – |
| 31 entity-dto-mapping | Kotlin / Mapping | Påbegynt | 3.0 | 2026-08-04 | case-31-forsoek-1 |
| 32 sealed-domain-modelling | Kotlin / DDD | Ikke startet | – | – | – |
| 33 collections-aggregation-report | Kotlin | Ikke startet | – | – | – |
| 34 null-safety-validation | Kotlin | Ikke startet | – | – | – |
| 35 kotlin-dsl-higher-order-functions | Kotlin | Ikke startet | – | – | – |
| 36 bank-transfer-fullstack-jpa | Full stack / JPA | Ikke startet | – | – | – |
| 37 kotlin-mapping-sealed-collections | Kotlin / Mapping | Ikke startet | – | – | – |
| 38 basic-spring-h2-rest | Full stack / JPA | Ikke startet | – | – | – |
| 39 wiremock-external-integration | Integrasjon | Ikke startet | – | – | – |
| 40 security-request-context | Sikkerhet | Ikke startet | – | – | – |

**Oppsummert:** 3 løst · 1 påbegynt · 36 ikke startet · 0 mestret

## Forsøkshistorikk

| Dato | Case | Forsøk | Branch | Score | Notater |
|---|---|---|---|---|---|
| 2026-07-27 | 01 | 1 | main | 7.5 | Invariants (Money, Quantity, Percentage), fast-rabatt-guard og NoDiscount-test løst. Gjenstående forbedringer: eksplisitte kanttester for 0 % og 100 %, tydelig avrundings-/skalakontrakt, og begrunnelse for trade-off mellom sealed class og Strategy Pattern. |
| 2026-08-01 | 02 | 1 | case-02-forsoek-1 | 8.9 | Løst med eksplisitt [from, to)-kontrakt, avvisning av from > to, lesbar datesUntil-implementasjon og ni grønne tester. Trekk for generelle testnavn, overflødig Java 9-konfigurasjon og mindre kodehygiene rundt isInWeekend()/whitespace. |
| 2026-08-04 | 26 | 1 | case-26-forsoek-1 | 9.2 | Alle seks funksjoner er refaktorert til lesbar, idiomatisk Kotlin. Den siste revisjonen innfører en typesikker `Seniority`-enum, en beregnet `Consultant.seniority`-property og én sentral klassifiserings- og valideringsregel i `Seniority.from()`. Sterk bruk av collections-API, null-safety, expression bodies, `when` og `joinToString`. Trekk fordi refaktoreringen endrer den opprinnelige offentlige kontrakten fra tekstetikett til `Seniority`, den case-insensitive byregelen ikke testes med ulik casing, overgangsgrensene er ufullstendig testet, og det gjenstår små formateringsdetaljer. |
| 2026-08-04 | 31 | 1 | case-31-forsoek-1 | 3.0 | Påbegynt. `fullName` og `isActiveOn` er korrekt og idiomatisk implementert som extension property/function med expression body; de to tilhørende testene passerer. Hele modulen er rød: 2 av 13 tester passerer, mens 11 feiler fordi TODO 3–6 fortsatt er uløst. Muntlig debrief er ikke vurdert. |

## Slik registrerer du et nytt forsøk

1. Lag branch: `git switch -c case-NN-forsoek-M main` (M = forsøksnummer).
2. Løs caset innen tidsboksen i casets README.
3. Kjør debrief-spørsmålene høyt — score reflekterer hvor godt du begrunner, ikke bare at testene er grønne.
4. Oppdater oversikten over (status, beste score, siste forsøk, branch) og legg til rad i historikken.
5. Commit STATUS.md på main.
