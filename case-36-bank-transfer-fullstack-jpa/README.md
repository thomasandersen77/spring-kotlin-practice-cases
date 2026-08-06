# Case 36 - Bankoverføring fullstack med Spring Boot, JPA og H2

## Domene
Bank / interne kontooverføringer

## Tid
90-120 minutter, gjerne fordelt på 2-3 økter. Dette er et større case enn 31-35 - målet er IKKE å skrive alt fra minnet i én live-coding-økt, men å trene hele backend-flyten stegvis, i den rekkefølgen TODO-ene er nummerert.

## Hva dette trener
- Full Spring Boot-flyt: HTTP-request → controller → applikasjonsservice → domene → repository-port → JPA-adapter → Hibernate → H2
- Tynne kontrollere: ingen forretningsregler, ingen `@Transactional`, ingen repository-kall i controlleren
- DTO-er som eksplisitt API-kontrakt, atskilt fra både domene og JPA-entiteter
- Eksplisitt mapping mellom DTO ↔ domene ↔ JPA (samme idé som case 31, nå i en ekte Spring/JPA-flyt)
- Ren domenemodell uten rammeverksannotasjoner (ingen Spring, JPA eller Jackson i `domain`-pakken)
- Aggregate root og invariants: saldo kan bare endres gjennom domenemetoder, aldri via en offentlig setter
- Applikasjonsservice som orkestrerer et use case mellom to aggregater, uten selv å manipulere saldo
- Repository-port og persistence-adapter (Dependency Inversion Principle i praksis)
- Spring Data JPA og Hibernate: `@Entity`, `@Version`, eksplisitte tabellnavn og kolonner
- `create-drop`-skjemalivssyklus i tester, og forskjellen på `create-drop`, `validate`, `update` og `none`
- Transaksjonsgrenser og rollback - og hvorfor testen som verifiserer dette ikke selv skal være `@Transactional`
- Optimistisk låsing med `@Version`
- DDD og SOLID brukt pragmatisk, uten overarkitektur (ingen CQRS, event sourcing, saga eller generiske baseklasser)

## Scenario
Banken tilbyr NOK-kontoer og interne overføringer mellom dem. En konto har en stabil `AccountId` (UUID), et eierNavn, status (`ACTIVE`/`BLOCKED`) og en saldo i øre. API-et snakker kroner med to desimaler (`BigDecimal`); domenet og databasen snakker bare hele øre (`Long`) - `Money` er broen mellom de to.

En intern overføring skal debitere avsender og kreditere mottaker gjennom domenemetodene, lagre begge kontoene og én overføringsrad, og gjøre alt dette atomisk i én Spring-transaksjon. Hvis noe feiler etter at avsender er debitert - inkludert selve lagringen av overføringsraden - skal HELE transaksjonen rulles tilbake. Ingen halvferdig overføring skal noensinne kunne ligge i databasen.

Alle sentrale funksjoner er markert med nummererte `TODO()`-er. Produksjonskoden kompilerer, Spring-konteksten starter, og H2/Hibernate-oppsettet fungerer allerede - det er forretningsreglene og mappingen som mangler.

## Arkitektur og flyt
```
web-adapter → application → domain
                       ↓
                 repository-port
                       ↑
              persistence-adapter
```

Pakkestruktur under `com.interview.case36.bank`:
- `domain` - `AccountId`, `Money`, `AccountStatus`, `BankAccount`, `TransferId`, `BankTransfer`, domeneunntak. Ingen avhengighet til Spring, JPA, Jackson eller web-DTO-er.
- `application` + `application.port` - `BankingService` (use cases) og portene `AccountRepository`/`TransferRepository`.
- `adapter.persistence` - JPA-entiteter, Spring Data-grensesnitt, adaptere som implementerer portene, og mapping domene ↔ JPA.
- `adapter.web` + `adapter.web.dto` + `adapter.web.mapping` - controller, `@RestControllerAdvice`, DTO-er og mapping DTO ↔ domene.
- `config` - `Clock`-bean, slik at `BankingService` aldri kaller `Instant.now()` direkte.

Regel: `application` avhenger av `AccountRepository`/`TransferRepository` (portene) - aldri av Spring Data direkte. `adapter.persistence` er det eneste laget som kjenner både domenet og JPA-entitetene.

## Oppgave
Løs TODO-ene i den nummererte rekkefølgen under. Rekkefølgen er ikke tilfeldig: senere TODO-er bygger på tidligere (f.eks. vil nesten alle tester som bruker penger feile med `NotImplementedError` fra `Money` helt til TODO 1 er løst - det er forventet, ikke en feil i scaffoldet).

## TODO / fokusområder
1. **Money-invariants og BigDecimal ↔ øre** (`domain/Money.kt`) - `ofKroner`/`toKroner`, eksakt konvertering, maks to desimaler, aldri negativt.
2. **`BankAccount.credit`** (`domain/BankAccount.kt`) - blokkert konto og positivt beløp.
3. **`BankAccount.debit`** (`domain/BankAccount.kt`) - blokkert konto, positivt beløp og tilstrekkelig dekning.
4. **Domene → JPA-mapping** (`adapter/persistence/PersistenceMapping.kt`, `toEntity()`-funksjonene) - husk å bevare eksisterende `@Version` ved oppdatering.
5. **JPA → domene-mapping** (samme fil, `toDomain()`-funksjonene) - bruk `BankAccount.reconstitute(...)`.
6. **Repository-adaptere** (`AccountPersistenceAdapter`, `TransferPersistenceAdapter`) - koble Spring Data til portene via mappingen over.
7. **Request DTO → command/domene-mapping** (`adapter/web/mapping/BankingWebMapping.kt`, `toCommand()`-funksjonene).
8. **Domene → response DTO-mapping** (samme fil, `toResponse()`-funksjonene).
9. **Opprett konto** (`BankingService.createAccount`) - skrivende transaksjon.
10. **Hent konto** (`BankingService.getAccount`) - read-only transaksjon, `AccountNotFoundException` ved manglende konto.
11. **Innskudd og transaksjonsgrense** (`BankingService.deposit`) - skrivende transaksjon rundt ett aggregat.
12. **Intern overføring og atomisk transaksjonsgrense** (`BankingService.transfer`) - casets viktigste use case. Skriv ned *hvor* transaksjonsgrensen ligger og *hvorfor*, før du implementerer.
13. **Tynn REST-controller** (`adapter/web/BankingController.kt`) - koble endepunktene til riktig use case og mapping.
14. **Exception mapping i `@RestControllerAdvice`** (`adapter/web/BankingExceptionHandler.kt`) - map `BankingException.code` til riktig HTTP-status.
15. **Kort debrief om DDD, SOLID og transaksjoner** - se `## DDD og SOLID i caset` og `## Intervjuspørsmål / debrief` under. Dette er ikke kode, men en muntlig/skriftlig oppsummering du bør kunne gi etter at du er ferdig.

## Akseptansekriterier
- Alle domene-, JPA-, service- og webtester er grønne på løsningsbranchen.
- Spring-konteksten starter med testprofilen (`@ActiveProfiles("test")`).
- Hibernate oppretter H2-tabellene med `create-drop`.
- En vellykket `transfer` endrer begge saldoer og lagrer nøyaktig én transfer-rad.
- Enhver feil i `transfer` - inkludert en feil i selve lagringen av overføringsraden - gir rollback av ALLE endringer i den transaksjonen.
- Ingen konto kan få negativ saldo.
- Blokkerte kontoer kan ikke brukes i noen transaksjon (verken debiteres eller krediteres).
- Controllerne inneholder ingen forretningslogikk og ingen repository-kall.
- `@Transactional` ligger i `BankingService`, ikke i controlleren eller persistence-adapteren.
- Domeneobjektene har ingen Spring-, JPA- eller Jackson-annotasjoner.
- JPA-entiteter returneres aldri fra controlleren.
- Ingen `Double` brukes for penger noe sted.
- Ingen `!!` i produksjonskoden.
- Ingen `ddl-auto=update` noe sted.
- Tester er uavhengige av rekkefølge og deler ikke utilsiktet data.
- Tomme/ugyldige requests gir stabile feilresponser - aldri stacktrace eller interne klassenavn.
- Koden kan forklares muntlig uten å gjemme seg bak rammeverket.

## H2 og Hibernate schema lifecycle
`spring.jpa.hibernate.ddl-auto=create-drop` oppretter skjemaet når `EntityManagerFactory`/Spring-konteksten starter, og fjerner det når konteksten lukkes. Spring **cacher** normalt testkonteksten mellom testmetoder - det betyr at "nytt skjema før hver testmetode" IKKE er standardoppførsel, selv med `create-drop`. De fleste testene i dette caset deler derfor kontekst og skjema med hverandre, og er skrevet for å tåle det (ferske, unikt UUID-baserte kontoer per test; delta-sammenligninger fremfor absolutte tellinger der det er relevant).

`HibernateSchemaLifecycleIntegrationTest` er den ENE, lille, dedikerte klassen som bruker `@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)` for å bevisst tvinge frem en ny kontekst - og dermed et nytt skjema - etter hver testmetode, nettopp for å demonstrere denne livssyklusen. Ingen andre testklasser i caset bruker `@DirtiesContext` på klassenivå; det ville gjort hele suiten unødvendig treg. `BankingServiceTransactionIntegrationTest` løser sitt isolasjonsbehov (en spy som skal stubbes i kun én test) med `Mockito.reset(...)` i en `finally`-blokk i stedet.

`validate` oppretter ingen tabeller - den kontrollerer bare at et allerede eksisterende skjema samsvarer med JPA-mappingen. `update` prøver å endre et eksisterende skjema inkrementelt og brukes ikke i dette caset (og bør sjeldent brukes noe sted). I produksjon bør Flyway eller Liquibase eie skjemaendringene, og Hibernate settes typisk til `validate` eller `none` - ikke `create-drop` eller `update`. Flyway/Liquibase er bevisst holdt utenfor dette caset for å holde fokus på controller → service → JPA → transaksjon.

## Transaksjonsgrenser
`@Transactional` skal ligge på offentlige metoder i `BankingService` - `createAccount`, `deposit` og `transfer` skrivende, `getAccount` som `@Transactional(readOnly = true)`. `transfer` er casets viktigste transaksjonsgrense: begge kontoene og overføringsraden skal lagres i én transaksjon, og ethvert runtime-unntak skal rulle tilbake alt - også kontoendringer som allerede er "lagret" via `repository.save(...)` tidligere i samme metodekall, siden de fortsatt bare er en del av den ene, ennå ikke committede transaksjonen.

To klassiske Spring-fallgruver som IKKE fungerer:
- Å legge `@Transactional` på en **privat** metode - Spring sin transaksjonshåndtering er proxy-basert, og proxyen ser aldri kallet inn til en privat metode.
- Å kalle en `@Transactional`-annotert metode på `this` fra en annen metode i samme klasse (self-invocation) - kallet går utenom proxyen, så ingen ny transaksjonsgrense åpnes.

`BankingServiceTransactionIntegrationTest` verifiserer den FAKTISKE transaksjonsgrensen ved å lese kontoene på nytt fra repository/databasen etter at servicekallet er ferdig - ikke ved å se på in-memory-objekter fra kallstedet. Testklassen er bevisst IKKE `@Transactional`: hvis den var det, kunne testens egen transaksjon skjule en manglende eller feilplassert `@Transactional` i servicen.

## DDD og SOLID i caset
**DDD:**
- `BankAccount` er aggregate root for én konto. Saldo er en invariant og kan ikke manipuleres utenom domenemetodene (`credit`/`debit`) - `balance` har privat setter.
- `Money` og `AccountId`/`TransferId` er value objects.
- En overføring mellom to kontoaggregater orkestreres av en applikasjonsservice (`BankingService`), ikke av ett av aggregatene selv - ingen av kontoene "vet om" den andre.
- Databasetransaksjonen sørger for teknisk atomisitet (alt eller ingenting skjer i databasen); domenemodellen sørger for at hver enkelt tilstandsendring underveis er gyldig (aldri en ugyldig saldo, aldri en operasjon på en blokkert konto).
- Ubiquitous language vises i navngivningen: `debit`, `credit`, `transfer`, `insufficient funds`, `blocked account` - ikke `updateBalance` eller `setAmount`.

**SOLID, pragmatisk:**
- **SRP** - controlleren håndterer HTTP, servicen orkestrerer use caset, domenet eier reglene, adapteren eier JPA.
- **OCP** - nye adaptere (f.eks. en annen database) kan legges til bak porten uten å endre `BankingService`, men det er ikke bygget noen ekstra abstraksjon for hypotetiske fremtidige behov.
- **LSP** - enhver `AccountRepository`-implementasjon må oppfylle portens kontrakt (f.eks. `findById` returnerer `null`, ikke kaster exception, for en manglende konto).
- **ISP** - portene er små og use-case-relevante (`AccountRepository`, `TransferRepository`), ikke ett stort "repository-interface for alt".
- **DIP** - `BankingService` avhenger av portene, ikke av `AccountJpaRepository` direkte.

**Utfordre deg selv:** Er to repository-porter faktisk verdt det i et så lite case? Ville det vært et pragmatisk nok valg å la `BankingService` bruke `AccountJpaRepository`/`TransferJpaRepository` (Spring Data) direkte, uten port og adapter? Det finnes ikke ett fasitsvar - men du bør kunne begrunne hvilken side du lander på, og hva som ville fått deg til å endre mening (flere persistensteknologier? behov for å teste servicen uten Spring i det hele tatt? et team som stadig bytter database?).

## Formål i intervjuet
Dette er det mest komplette caset i repoet ved siden av case 30: det trener hele stacken fra HTTP til database, med et use case (intern overføring) som tvinger frem et ekte spørsmål om transaksjonsgrenser og atomisitet - noe som er svært vanlig i backend-intervjuer for erfarne utviklere. Kan du forklare *hvorfor* `@Transactional` ligger der den ligger, *hva* som faktisk beskytter mot en halvferdig overføring, og *hvordan* du beviser det med en test - uten at testen selv jukser ved å være `@Transactional` - er det et sterkt signal.

## Ikke gjør det for lett
- Ikke flytt reglene til controlleren for å få testene grønne.
- Ikke returner JPA-entiteter direkte som JSON.
- Ikke sett saldo direkte i servicen (`account.balanceOre = ...` finnes ikke, og skal ikke finnes).
- Ikke bruk `@Transactional` på testmetoden i `BankingServiceTransactionIntegrationTest` for å skjule en manglende service-transaksjon.
- Ikke gjør alle klassene `open` manuelt - `kotlin-maven-allopen`/`kotlin-maven-noarg`-pluginene (allerede konfigurert i `pom.xml`) håndterer det Spring/JPA faktisk trenger.
- Ikke bruk H2-konsollen eller manuelle SQL-innsettinger som erstatning for repository-flyten.
- Ikke bruk `ddl-auto=update`.
- Ikke bruk mocks i fullstack-/webtestene - de skal gå gjennom ekte Spring, ekte JPA og ekte H2.
- Ikke implementer idempotens, pessimistisk låsing eller distribuert saga i første forsøk - det er bevisst utenfor scope her.

## Intervjuspørsmål / debrief
1. Hvorfor ligger `@Transactional` på applikasjonsservicen og ikke controlleren eller repository-adapteren?
2. Hva skjer dersom mottakeren ikke finnes etter at avsenderen allerede er lest eller endret?
3. Hvordan beviser testen at rollback faktisk skjedde i databasen - og ikke bare i et in-memory-objekt?
4. Hvorfor skal ikke service-integrasjonstesten selv være `@Transactional`?
5. Hva er forskjellen mellom `create`, `create-drop`, `validate`, `update` og `none` i Hibernate?
6. Hvorfor er `create-drop` egnet her, men vanligvis feil i produksjon?
7. Hvem bør eie databaseskjemaet i produksjon: Hibernate eller Flyway/Liquibase?
8. Hvorfor er domenemodellen separat fra JPA-entiteten? Hva koster det i ekstra kode, og hva får du igjen?
9. Hvor går grensen mellom request-validering (Bean Validation) og domeneinvariants?
10. Hvorfor er `Long` i øre tryggere enn `Double` for penger?
11. Hva beskytter `@Version` mot, og hva beskytter den IKKE mot?
12. Hva skjer med Spring-transaksjoner ved self-invocation?
13. Når ville `REQUIRES_NEW` vært riktig, og hvorfor er det feil for selve debit/credit-flyten her?
14. Er repository-portene pragmatisk arkitektur eller unødvendig abstraksjon i dette caset?
15. Hvordan ville dette endret seg med PostgreSQL, flere appinstanser og samtidige overføringer?
16. Hvordan ville du håndtert idempotens, slik at samme transfer-request ikke utføres to ganger ved en dobbel innsending fra klienten?
17. Hva bør være aggregate-grensen når én use case endrer to kontoer - én felles "Transfer"-aggregat, eller to separate `BankAccount`-aggregater koordinert av en service?
18. Hvilke deler av løsningen er genuint DDD, og hvilke deler er bare vanlig, god lagdeling?

## Anbefalt arbeidsrekkefølge
1. Les hele README-en og skumles gjennom alle TODO-kommentarene i koden før du skriver noe.
2. Løs TODO 1 (`Money`) og få `MoneyTest` grønn. Ikke gå videre før denne er grønn - alt annet bygger på den.
3. Løs TODO 2-3 (`BankAccount.credit`/`debit`) og få `BankAccountTest` grønn.
4. Løs TODO 4-6 (mapping og persistence-adaptere) og få `AccountPersistenceTest` grønn.
5. Løs TODO 9-12 (use cases i `BankingService`) og få `BankingServiceTransactionIntegrationTest` grønn - dette er kjernen i caset. Bruk ekstra tid på TODO 12.
6. Løs TODO 7-8, 13-14 (web-mapping, controller, exception handler) og få `BankingControllerIntegrationTest` grønn.
7. Se på `HibernateSchemaLifecycleIntegrationTest` og forklar høyt hva den demonstrerer.
8. Avslutt med TODO 15: gå gjennom `## DDD og SOLID i caset` og `## Intervjuspørsmål / debrief` og svar høyt, som om intervjueren satt ved siden av deg.

## Kommandoer

```bash
./mvnw test -pl case-36-bank-transfer-fullstack-jpa
./mvnw spring-boot:run -pl case-36-bank-transfer-fullstack-jpa
```
