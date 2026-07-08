# Sopra Kotlin interview cases

Dette er en samling små, uavhengige case-prosjekter for å øve til teknisk intervju/parprogrammering.

> TODO 1: Presenter og begrunn designvalgene dine som i et seniorintervju: hvor domeneregler hører hjemme, hvilke abstraksjoner som er valgt, og hvilke trade-offs som er akseptable.
> TODO 2: Gjennomfør hvert case i realistisk intervju-modus på ca. 60 minutter, lever en tydelig minimumsløsning innen tidsrammen, og oppsummer konkrete forbedringspunkter etter økten.

## Prosjekter

1. `case-01-order-api-core-db`
   - REST API
   - core/domain
   - service
   - repository-port
   - JPA/H2
   - mapping mellom domain og entity

2. `case-02-refactor-fat-controller`
   - eksisterende kodebase
   - feit controller
   - refaktorering
   - domain/service/DTO
   - tester

3. `case-03-anti-corruption-layer`
   - ekstern integrasjon
   - anti-corruption layer
   - mapping fra ekstern DTO til domenebegrep
   - domenepolicy

4. `case-04-current-user-rbac`
   - tilgangskontroll
   - CurrentUser
   - RBAC
   - testbar access policy

5. `case-05-pure-kotlin-domain`
   - ren Kotlin
   - value classes
   - sealed classes
   - domenemodellering
   - tester

6. `case-06-debug-and-test`
   - debugging
   - off-by-one-feil
   - test først
   - refaktorering

7. `case-07-business-rules-kata`
   - forretningsregler
   - prioritering av regler
   - tester for edge cases
   - refaktorering av regellogikk

8. `case-08-shipping-slot-aggregate`
   - logistikk / levering
   - aggregate root + value objects
   - invariants og sortering av bookinger

9. `case-09-hospital-triage-policy`
   - helse / triage
   - domain service og lesbare regler
   - guard clauses og edge cases

10. `case-10-payment-settlement-strategy`
    - betaling / oppgjør
    - strategy pattern + OCP
    - gebyrberegning med money value object

11. `case-11-library-loan-domain`
    - bibliotekdomene
    - entity + value objects
    - utlånsregler, forlengelse og overdue-beregning

12. `case-12-parking-pricing-rules`
    - parkering
    - business rules og avrunding
    - rabatter, makspris og grenseverdier

13. `case-13-warehouse-pick-list`
    - lager / ordreplukk
    - aggregate root med state transitions
    - invariants for SKU, quantity og fullføring

14. `case-14-insurance-claim-acl`
    - forsikring
    - anti-corruption layer
    - robust DTO-mapping og inputvalidering

15. `case-15-flight-seat-booking`
    - flybooking
    - aggregate + use case
    - setereservasjon, kansellering og domain event

16. `case-16-energy-tariff-billing`
    - strømavregning
    - presis beregning med BigDecimal
    - tariff, moms og avrundingsregler

17. `case-17-incident-escalation-state-machine`
    - incident management
    - state machine med gyldige overganger
    - overgangshistorikk med actor og timestamp

18. `case-18-feature-flag-rbac`
    - feature flags / policy
    - RBAC og miljøregler
    - testbar tilgangskontroll uten tung security-stack

19. `case-19-restaurant-reservation-api`
    - restaurantbooking API
    - tynn controller + application service
    - DTO-validering og MockMvc-tester

20. `case-20-ecommerce-cart-checkout`
    - e-handel
    - aggregate root + persistence boundary
    - JPA/H2, mapping og transaksjonsgrenser

21. `case-21-subscription-proration`
    - SaaS-abonnement
    - proration-beregning og dato/tid
    - refaktorering av med vilje rotete servicekode

22. `case-22-iot-sensor-alerting`
    - IoT / sensordata
    - ports/adapters + strategy pattern
    - alert-regler og testbare use cases

23. `case-23-optimistic-locking-concurrency`
    - samtidighet / konflikthåndtering
    - optimistic locking og versjonsfelt
    - aggregate-konsistens under konkurrerende oppdateringer

24. `case-24-domain-events-outbox`
    - domenhendelser / integrasjon
    - transactional outbox-mønster
    - tydelig grense mellom domeneevent og publiserbar melding

25. `case-25-idempotent-command-processing`
    - idempotency / retries
    - kommandohåndtering med idempotency key
    - robusthet ved duplikate kall mot ekstern gateway

## Vanskelighetsprogresjon (3 nivåer)

### Nivå 1 - Foundation
Mål: trygghet i grunnleggende domenemodellering, testbarhet og tydelige laggrenser.

- `case-05-pure-kotlin-domain`
- `case-06-debug-and-test`
- `case-07-business-rules-kata`
- `case-11-library-loan-domain`
- `case-12-parking-pricing-rules`
- `case-19-restaurant-reservation-api`

### Nivå 2 - Intermediate
Mål: bedre strukturvalg i applikasjonslag, aggregates og policy/strategi-design.

- `case-01-order-api-core-db`
- `case-02-refactor-fat-controller`
- `case-04-current-user-rbac`
- `case-08-shipping-slot-aggregate`
- `case-09-hospital-triage-policy`
- `case-10-payment-settlement-strategy`
- `case-13-warehouse-pick-list`
- `case-15-flight-seat-booking`
- `case-16-energy-tariff-billing`
- `case-17-incident-escalation-state-machine`
- `case-18-feature-flag-rbac`
- `case-20-ecommerce-cart-checkout`
- `case-21-subscription-proration`
- `case-22-iot-sensor-alerting`

### Nivå 3 - Advanced
Mål: robuste designvalg for integrasjon, konsistens og feilscenarioer i “ekte” systemer.

- `case-03-anti-corruption-layer`
- `case-14-insurance-claim-acl`
- `case-23-optimistic-locking-concurrency`
- `case-24-domain-events-outbox`
- `case-25-idempotent-command-processing`

## Hvordan velge neste case

### Velg neste case innen samme nivå
- Start med caset som treffer svakeste område fra forrige debrief (f.eks. invariants, teststrategi, eller laggrenser).
- Veksle mellom “domene-tunge” og “API/integrasjon”-tunge case for bredere trening.
- Når et case er bestått, velg neste ubrukte case i samme nivå før du vurderer nivåhopp.
- Ikke hopp nivå bare fordi ett case gikk bra; bruk passkriteriene under.

### Passkriterier per caseøkt (60 min)
Et case regnes som bestått når alle punkter under er oppfylt:

- Du leverer en fungerende minimumsløsning innen 60 minutter.
- Minst én happy-path-test og én viktig edge/negativ test er grønne.
- Du kan forklare grensene mellom domain, application service og adapter uten å lese fra koden.
- Du avslutter med kort debrief: hva ble bevisst utelatt, og hva ville vært neste forbedring.

### Kriterier for å gå fra Nivå 1 til Nivå 2
- Du har gjennomført minst 4 ulike Foundation-case.
- Minst 3 av de siste 4 Foundation-case er bestått etter passkriteriene over.
- I minst 2 av disse casene har du lagt til eller strammet inn en eksplisitt domeneregel/invariant.

### Kriterier for å gå fra Nivå 2 til Nivå 3
- Du har gjennomført minst 5 ulike Intermediate-case.
- Minst 4 av de siste 5 Intermediate-case er bestått etter passkriteriene over.
- Du har vist minst ett case med tydelig konflikthåndtering, feilmodellering eller bevisst integrasjonsgrense.

### Hvis kriteriene ikke er oppfylt
- Bli på samme nivå i 2-3 nye case.
- Velg case som trener nøyaktig det som manglet i forrige debrief.
- Mål framgang på forklaringsevne og testkvalitet, ikke bare på antall grønne tester.

## Realistisk intervju-modus (60 minutter for alle case)

Bruk samme format uansett case, slik at øving og evaluering blir sammenlignbar:

1. **0-5 min - Problemforståelse**
   - Les README og avklar antakelser høyt.
   - Velg minimal leveranse for “green baseline”.
2. **5-15 min - Designskisse + teststrategi**
   - Definér domenegrenser og ansvar (domain/application/adapter).
   - Velg 1-2 viktigste tester først (happy path + kritisk edge case).
3. **15-40 min - Implementer minimumsløsning**
   - Få en sammenhengende flyt til å virke.
   - Hold koden enkel og forklarbar, unngå overdesign.
4. **40-50 min - Stram inn kvalitet**
   - Ta viktigste negative flyt.
   - Rydd navn, duplikasjon og invariants.
5. **50-60 min - Debrief som i intervju**
   - Forklar hva som er gjort, hva som er bevisst utelatt.
   - Beskriv neste forbedringssteg hvis du fikk 30 min ekstra.

## Kjør alle casene samlet

Fra repo-roten kan du kjøre alle moduler via topp-POM:

```bash
mvn clean verify
```

Hvis du vil verifisere bygg uten å kjøre tester:

```bash
mvn clean verify -DskipTests
```

## Anbefalt øvingsform

For hvert case:

1. Les README.
2. Kjør `mvn test`.
3. Se hvilke tester som feiler eller mangler.
4. Fullfør TODO-er.
5. Tenk høyt som i parprogrammering.
6. Forklar tradeoffs:
   - Hva ville du gjort i ekte prosjekt?
   - Hva ville du forenklet i et intervju?
   - Hvor går transaksjonsgrensen?
   - Hva er domain, application service og adapter?
   - Hvordan ville du testet dette?

## Viktig

Prosjektene er med vilje små og uferdige. Målet er ikke å pugge løsningene, men å trene på:
- Kotlin-syntaks
- Spring Boot
- REST
- JPA/Hibernate
- DDD-light
- SOLID
- Clean Code
- testbarhet
- parprogrammering og høyttenkning
