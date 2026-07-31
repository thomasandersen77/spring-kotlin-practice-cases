# Case 30 - Bank account CRUD + JPA
Lite banksystem for intervjuøving med Kotlin, Spring Boot, REST, JPA og DDD-light.
## Scenario
En liten bank trenger et internt API for administrasjon av kunder og bankkontoer. Løsningen skal støtte CRUD for kunder og kontoer, men saldo skal kun endres via eksplisitte domeneoperasjoner (`deposit`/`withdraw`) og ikke via generell `PUT`.
## Læringsmål
- idiomatisk Kotlin
- REST CRUD
- Bean Validation
- JPA entities
- `ManyToOne(fetch = LAZY)`
- unike constraints
- repository-porter (ports/adapters)
- transaksjonsgrenser med `@Transactional`
- H2-testing
- domain invariants
- forskjellen mellom CRUD-atferd og domeneoperasjoner
## Oppgave (prioritert)
1. Implementer `Money`-normalisering med skala = 2 og konsistent avrunding (`HALF_EVEN`).
2. Fullfør domeneregler i `BankAccount.deposit`, `withdraw` og `close`.
3. Sørg for at utilstrekkelig saldo gir tydelig domeneexception.
4. Implementer filtrering i `findAllByCustomerId`.
5. Implementer faktisk innskuddsendepunkt (`POST /api/accounts/{id}/deposits`).
6. Fullfør oppdatering av kunde med e-postkonfliktregler.
7. Verifiser sletting av kunde med kontoer og sletting av konto med saldo.
8. Fullfør mapping- og valideringsregler der TODO er markert.
9. Forbedre global feilhåndtering slik at feilkontrakten er konsekvent i alle lag.
10. Gjør alle testene grønne uten å bryte lagdeling mellom domain/application/api/persistence.
## Akseptansekriterier
- Domenet avhenger ikke av Spring, HTTP, JSON, JPA eller H2.
- Konto opprettes alltid med `0,00 NOK`.
- Saldo kan ikke oppdateres via `PUT /api/accounts/{id}`.
- Innskudd/uttak må være positive beløp.
- Uttak kan ikke gi negativ saldo.
- Lukket konto kan ikke motta innskudd eller uttak.
- Konto med saldo kan ikke lukkes eller slettes.
- Kunde med kontoer kan ikke slettes.
- E-post og kontonummer er unike.
- `Money` bruker `BigDecimal` med eksplisitt skala/avrunding.
- `open-in-view` er slått av.
- Testene dekker domene, repository, application service og REST-integrasjon.
## Kjøring
```bash
mvn test
mvn spring-boot:run
```
Fra rotprosjekt:
```bash
mvn test -pl case-30-bank-account-crud-jpa
```
## Nyttige URL-er
- http://localhost:8080/api/customers
- http://localhost:8080/api/accounts
- http://localhost:8080/h2-console
## Intervjuspørsmål
1. Hvorfor bør ikke saldo oppdateres gjennom vanlig `PUT`?
2. Hvor bør regelen om utilstrekkelig saldo ligge?
3. Hvorfor er domenemodellen skilt fra JPA-entitetene?
4. Hvorfor brukes `BigDecimal` i stedet for `Double`?
5. Hvorfor brukes `FetchType.LAZY`?
6. Hva er problemet med Open Session in View?
7. Hvor bør `@Transactional` plasseres?
8. Hva er forskjellen på database-constraint og domeneinvariant?
9. Hvorfor bør JPA-entiteter normalt ikke være Kotlin `data class`?
10. Hva skjer ved to samtidige uttak?
11. Hvordan ville løsningen blitt endret for PostgreSQL?
12. Hvorfor er hard sletting av kontoer problematisk i et ekte banksystem?
13. Hvordan ville du implementert revisjonsspor?
14. Hvilke trade-offs finnes mellom separate domeneobjekter og JPA-entiteter?
15. Hvordan ville du testet databasen mot ekte PostgreSQL med Testcontainers?
## Notat om JPA-entiteter
JPA-entiteter er bevisst ikke `data class`. Auto-generert `equals/hashCode/toString` kan gi uønsket adferd med mutable entiteter og lazy-relasjoner.
## Forenkling i dette caset
Hard sletting av bankkontoer er en bevisst forenkling for CRUD-trening. I produksjon ville ofte stenging, arkivering og revisjonsspor vært mer passende.
