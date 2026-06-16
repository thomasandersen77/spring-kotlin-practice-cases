# Sopra Kotlin interview cases

Dette er en samling små, uavhengige case-prosjekter for å øve til teknisk intervju/parprogrammering.

> TODO 1: Presenter og begrunn designvalgene dine som i et seniorintervju: hvor domeneregler hører hjemme, hvilke abstraksjoner som er valgt, og hvilke trade-offs som er akseptable.
> TODO 2: Gjennomfør hvert case under realistisk tidspress (45/60/90 min), lever en tydelig minimumsløsning innen tidsrammen, og oppsummer konkrete forbedringspunkter etter økten.

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
