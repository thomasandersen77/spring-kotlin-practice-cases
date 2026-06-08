# Sopra Steria Kotlin/Spring Boot interview cases

Dette er en samling små, uavhengige case-prosjekter for å øve til teknisk intervju/parprogrammering.

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
