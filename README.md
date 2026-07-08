# Sopra Kotlin interview cases
Dette repoet er en strukturert treningsarena for tekniske Kotlin-intervjuer og parprogrammering.
Alle case er designet for manuell problemløsning, tydelig høyttenkning, og bevisste designvalg under tidspress.

## Målet med repoet
- Trene **Domain-Driven Design (DDD-light)** i små, konkrete oppgaver.
- Trene **SOLID** med praktiske trade-offs i stedet for teoretiske svar.
- Trene **Clean Code**: navn, ansvar, grenser, og lesbarhet.
- Trene **idiomatisk Kotlin**: null-safety, value classes, sealed types, immutable modeller.
- Trene intervjuferdighet: levere minimumsløsning og forklare hva som bør forbedres videre.

## Standard intervjuformat (60 min for alle case)
Bruk samme format på alle case for sammenlignbar progresjon:

1. **0-5 min - Problemforståelse**
   - Les case-README høyt.
   - Avklar antakelser.
   - Definer en minimal “green baseline”.
2. **5-15 min - Designskisse + teststrategi**
   - Beskriv ansvar mellom domain, application service og adapter.
   - Velg første tester (happy path + kritisk edge case).
3. **15-40 min - Implementer minimumsløsning**
   - Få en fungerende ende-til-ende flyt.
   - Prioriter tydelighet over “smart” abstraksjon.
4. **40-50 min - Stram inn kvalitet**
   - Dekk viktig negativ flyt.
   - Rydd navn, duplikasjon og invariants.
5. **50-60 min - Debrief**
   - Hva ble gjort?
   - Hva ble bevisst utsatt?
   - Hva er neste forbedring med +30 min?

## Underprosjekter i anbefalt løsningsrekkefølge (lavest -> høyest kompleksitet)

1. `case-01-pure-kotlin-domain`
   - Vanskelighetsgrad: **Foundation**
   - Fokus: ren Kotlin-domenemodellering, value classes, sealed classes, rabattregler.

2. `case-02-debug-and-test`
   - Vanskelighetsgrad: **Foundation**
   - Fokus: test-først, off-by-one, dato-semantikk, lesbar refaktorering.

3. `case-03-business-rules-kata`
   - Vanskelighetsgrad: **Foundation**
   - Fokus: regelprioritering, edge cases, enkel men robust regellogikk.

4. `case-04-library-loan-domain`
   - Vanskelighetsgrad: **Foundation**
   - Fokus: entity/value objects, tilstandsregler, dato- og overdue-logikk.

5. `case-05-parking-pricing-rules`
   - Vanskelighetsgrad: **Foundation**
   - Fokus: pricing-regler, avrunding, grenseverdier, tydelige tester.

6. `case-06-restaurant-reservation-api`
   - Vanskelighetsgrad: **Foundation**
   - Fokus: tynn API-grense, use case-design, inputvalidering.

7. `case-07-order-api-core-db`
   - Vanskelighetsgrad: **Intermediate**
   - Fokus: domain/service/repository-port, JPA-grenser, statusoverganger.

8. `case-08-refactor-fat-controller`
   - Vanskelighetsgrad: **Intermediate**
   - Fokus: refaktorering av ansvar, DTO vs domene, testbar prising.

9. `case-09-current-user-rbac`
   - Vanskelighetsgrad: **Intermediate**
   - Fokus: RBAC-policy, current user, testbar tilgangskontroll.

10. `case-10-shipping-slot-aggregate`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: aggregate invariants, kapasitet, duplikatregler, sortering.

11. `case-11-hospital-triage-policy`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: deterministic policy, guard clauses, regelrekkefølge.

12. `case-12-payment-settlement-strategy`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: Strategy/OCP, DIP, gebyrregler med Money.

13. `case-13-warehouse-pick-list`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: aggregate state transitions, complete-regler, invariant-håndheving.

14. `case-14-flight-seat-booking`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: bookingkonsistens, kansellering, domain event-intensjon.

15. `case-15-energy-tariff-billing`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: BigDecimal-presisjon, momsregler, forutsigbar avrunding.

16. `case-16-incident-escalation-state-machine`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: gyldige overganger, historikk, eksplisitt feilhåndtering.

17. `case-17-feature-flag-rbac`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: policy-matrise (rolle/miljø/godkjenning), testbar beslutningslogikk.

18. `case-18-ecommerce-cart-checkout`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: cart-invariants, checkout-overganger, applikasjonsgrense.

19. `case-19-subscription-proration`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: inkluderende datoer, proratering, money/avrunding.

20. `case-20-iot-sensor-alerting`
    - Vanskelighetsgrad: **Intermediate**
    - Fokus: ports/adapters, terskelstrategier, alert/no-alert-flyt.

21. `case-21-anti-corruption-layer`
    - Vanskelighetsgrad: **Advanced**
    - Fokus: ekstern DTO-oversettelse, policy-separasjon, rent domenespråk.

22. `case-22-insurance-claim-acl`
    - Vanskelighetsgrad: **Advanced**
    - Fokus: robust ACL-mapping, validering, eksplisitte failure-resultater.

23. `case-23-optimistic-locking-concurrency`
    - Vanskelighetsgrad: **Advanced**
    - Fokus: versjonering, konfliktresultat, konsistens ved samtidighet.

24. `case-24-domain-events-outbox`
    - Vanskelighetsgrad: **Advanced**
    - Fokus: domain event vs outbox message, transaksjonsgrense, atomisitet.

25. `case-25-idempotent-command-processing`
    - Vanskelighetsgrad: **Advanced**
    - Fokus: idempotency key, retry-sikkerhet, kontrollert gateway-kall.

## Vanskelighetsnivå forklart
- **Foundation:** primært domain + testbarhet, lav infrastrukturkompleksitet.
- **Intermediate:** flere laggrenser og designvalg (policy, strategy, aggregate behavior).
- **Advanced:** integrasjonsrobusthet, konsistens, samtidighet, og feilhåndtering i realistiske scenarioer.

## Hvordan velge neste case

### Regel for neste case
- Etter bestått case: velg neste **ubrukte** case i samme nivå.
- Velg case som trener svakeste punkt fra siste debrief.
- Ikke hopp nivå basert på én god økt.

### Passkriterier per caseøkt (60 min)
Et case er bestått når alle punkter er oppfylt:
- Fungerende minimumsløsning innen 60 minutter.
- Minst én happy-path-test og én relevant negativ/edge-test er grønn.
- Du kan forklare domain/application/adapter-grenser uten å lese direkte fra kode.
- Du leverer en kort debrief med bevisst utelatelser og neste forbedring.

### Kriterier for nivåhopp
- **Nivå 1 -> Nivå 2**
  - Minst 4 ulike Foundation-case gjennomført.
  - Minst 3 av de siste 4 Foundation-case bestått.
  - Minst 2 case med tydelig invariant/domeneregel lagt til eller forbedret.

- **Nivå 2 -> Nivå 3**
  - Minst 5 ulike Intermediate-case gjennomført.
  - Minst 4 av de siste 5 Intermediate-case bestått.
  - Minst 1 case med tydelig konflikthåndtering, feilmodellering, eller integrasjonsgrense forklart godt.

### Hvis kriteriene ikke er oppfylt
- Fortsett 2-3 case på samme nivå.
- Velg målrettede case mot konkrete svakheter.
- Mål forbedring på forklaringsevne og testkvalitet, ikke bare antall grønne tester.

## Hurtigkommandoer

Kjør alle case fra root:
```bash
mvn clean verify
```

Verifiser bygg uten tester:
```bash
mvn clean verify -DskipTests
```

Kjør ett enkelt case:
```bash
mvn -pl <modulnavn> test
```

Eksempel:
```bash
mvn -pl case-10-shipping-slot-aggregate test
```

## Debrief-mal (anbefalt etter hver økt)
- Hva implementerte du ferdig?
- Hvilke invariants/regler ble tydelige?
- Hvor er laggrensene (domain/application/adapter)?
- Hva var viktigste trade-off under tidspress?
- Hva ville du gjort med +30 minutter?

## Viktig
Prosjektene er med vilje små og uferdige. Målet er ikke å pugge løsninger, men å trene:
- Kotlin-syntaks og idiomer
- Spring Boot og REST-grenser
- DDD-light og SOLID
- Clean Code og testbarhet
- tydelig høyttenkning i intervjusituasjon
