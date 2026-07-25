# Sopra Steria Kotlin/Spring Boot interview cases

Dette er en samling små, uavhengige case-prosjekter for å øve til teknisk intervju/parprogrammering.

## Prosjekter

1. `case-01-pure-kotlin-domain` - ren Kotlin domenemodellering: value classes, sealed classes og money-beregning uten Spring.
2. `case-02-debug-and-test` - debugging og testbarhet: finn og avklar en off-by-one-feil i en kapasitetsberegner før refaktorering.
3. `case-03-business-rules-kata` - business rules kata: forretningsregler for fakturaberegning (rabatter, VIP, koder).
4. `case-04-library-loan-domain` - library loan domain: entity/value object og regler for forlengelse og forfalt lån.
5. `case-05-parking-pricing-rules` - parking pricing rules: domain service for prising med avrunding og tidsintervaller.
6. `case-06-restaurant-reservation-api` - restaurant reservation API: thin controller, validation og use case for bordreservasjon.
7. `case-07-order-api-core-db` - order API via core-lag til database: aggregatrot, ordrelinjer og JPA som persistensdetalj.
8. `case-08-refactor-fat-controller` - refaktorer en feit controller i en abonnementsmodul med blandede ansvarsområder.
9. `case-09-current-user-rbac` - CurrentUser og tilgangskontroll: testbar RBAC uten å blande inn Spring Security direkte.
10. `case-10-shipping-slot-aggregate` - shipping slot aggregate: kapasitet, value objects og invariants for leveringsbookinger.
11. `case-11-hospital-triage-policy` - hospital triage policy: domain service som prioriterer pasienter etter symptomer og vitale tegn.
12. `case-12-payment-settlement-strategy` - payment settlement strategy: strategy pattern for gebyrberegning per betalingsmetode.
13. `case-13-warehouse-pick-list` - warehouse pick list: aggregate med statusoverganger for plukklister.
14. `case-14-flight-seat-booking` - flight seat booking: aggregate root med konsistensgrense og domain event for setereservasjon.
15. `case-15-energy-tariff-billing` - energy tariff billing: presise value objects og avrunding for strømavregning.
16. `case-16-incident-escalation-state-machine` - incident escalation state machine: gyldige statusoverganger med historikk/audit.
17. `case-17-feature-flag-rbac` - feature flag RBAC: access policy-regelmatrise for roller, miljø og produktområde.
18. `case-18-ecommerce-cart-checkout` - ecommerce cart checkout: aggregate for handlekurv med transaksjons-/persistensgrense.
19. `case-19-subscription-proration` - subscription proration: dato- og money-beregning for midt-i-periode-oppgradering.
20. `case-20-iot-sensor-alerting` - IoT sensor alerting: ports/adapters og strategy pattern for terskelbaserte alerts.
21. `case-21-anti-corruption-layer` - anti-corruption layer: ekstern kredittintegrasjon oversatt til domenespråk uten lekkasje.
22. `case-22-insurance-claim-acl` - insurance claim ACL: DTO-mapping og validering av ekstern skadedata til domenemodell.
23. `case-23-optimistic-locking-concurrency` - versjonskontroll og konflikthåndtering ved samtidige reservasjoner.
24. `case-24-domain-events-outbox` - domain events outbox: transactional outbox for konsistente domene-events ved ordreplassering.
25. `case-25-idempotent-command-processing` - idempotency keys for retry-sikker betalingshåndtering.

Hvert case har sin egen README.md med scenario, TODO-er og akseptansekriterier.

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
