# Case 41 - Unit testing med Mockito, MockK og TDD

## Domene
Lojalitetspoeng for kundekjøp

## Tid
60–90 minutter

## Vanskelighetsgrad
Lav til medium

## Hva dette trener
Læringsmål for dette caset:
- opprette JUnit Jupiter-testklasser og testmetoder fra scratch
- strukturere tester med Arrange–Act–Assert
- bruke AssertJ til objekt-, tall- og exception-assertions
- stubbe returverdier med både Mockito og MockK
- verifisere viktige kall og fravær av kall uten å overspesifisere testen
- teste happy path, grenser, validering og feilflyt
- forstå forskjellen mellom state verification og interaction verification
- gjennomføre red–green–refactor når en forretningsregel endres

## Scenario
En lojalitetstjeneste gir ett grunnpoeng per 1 000 øre i et kjøp. `STANDARD`-kunder får bare grunnpoengene, mens `PLUS`-kunder får like mange bonuspoeng og dermed dobbelt antall poeng. Hele poengsummen krediteres i en ekstern poengbok.

Produksjonskoden er implementert. Det finnes med vilje ingen ferdige tester: du skal opprette testklassene, velge testdata, konfigurere mocks og formulere assertions selv.

## Oppgave
Lag to uavhengige enhetstestsuiter for `LoyaltyPointsService`:

1. `LoyaltyPointsServiceMockitoTest` med Mockito.
2. `LoyaltyPointsServiceMockKTest` med MockK.

Bruk JUnit Jupiter i begge og AssertJ for alle assertions. Du kan teste de samme reglene i begge suitene; repetisjonen er en del av treningen.

Når den eksisterende kontrakten er dekket og grønn, skal du endre poengregelen testdrevet slik det er beskrevet i TODO 6.

## Gjeldende kontrakt
- blank `customerId` avvises med `IllegalArgumentException`
- `amountOre <= 0` avvises med `IllegalArgumentException`
- ukjent kunde gir `CustomerNotFoundException`
- ett grunnpoeng gis per hele 1 000 øre; resten avrundes ned
- `STANDARD` gir ingen bonus
- `PLUS` gir bonus lik antall grunnpoeng
- poengboken krediteres én gang med totalen når totalen er større enn null
- poengboken skal ikke kalles når kjøpet gir null poeng eller valideringen feiler

## TODO / fokusområder
- TODO 1: Opprett Mockito-testklassen med `@ExtendWith(MockitoExtension::class)` og mocks for begge portene.
- TODO 2: Test minst STANDARD, PLUS, ukjent kunde og et kjøp under poenggrensen med Mockito.
- TODO 3: Opprett en separat MockK-testklasse og test de samme hovedreglene med `mockk`, `every` og `verify`.
- TODO 4: Legg til valideringstester. Bruk AssertJ til å sjekke exception-type og relevant melding.
- TODO 5: Verifiser bare domenerelevante interaksjoner: korrekt kreditering og tilfellene der poengboken ikke skal kalles.
- TODO 6: Gjennomfør en ny regel med TDD: Et PLUS-kjøp på minst 50 000 øre skal gi tredobbelt totalpoeng i stedet for dobbelt. Kjøp på 49 999 øre skal fortsatt følge gammel PLUS-regel. Skriv eller endre grensetestene først, se dem feile, implementer minst mulig kode og refaktorer til slutt.
- TODO 7: Endre én eksisterende forventning bevisst og observer at testen feiler av riktig grunn. Sett den deretter tilbake før du avslutter.

## Forslag til arbeidsrekkefølge
1. Kjør testen og bekreft at bygget er grønt med null tester.
2. Lag én Mockito happy-path-test og få den grønn.
3. Utvid Mockito-suiten med feil- og interaksjonstester.
4. Gjenta kontrakten med MockK for å kjenne forskjellen i API-ene.
5. Gjennomfør TODO 6 som en separat red–green–refactor-syklus.
6. Kjør hele modulen og les testrapporten, ikke bare den grønne markeringen i IDE-en.

## Akseptansekriterier
- Minst fire meningsfulle tester bruker Mockito.
- Minst fire meningsfulle tester bruker MockK.
- JUnit Jupiter brukes som testmotor og livssyklus.
- AssertJ brukes for alle assertions; ingen assertions fra JUnit eller Kotlin.
- Begge suitevariantene dekker minst én happy path, én feilflyt og én interaksjon.
- Grensene 49 999 og 50 000 øre er dekket etter TDD-endringen.
- Ingen test bruker Spring-kontekst; dette er raske enhetstester.
- Alle tester i modulen er grønne etter at oppgaven er ferdig.
- Testnavnene beskriver observerbar oppførsel, ikke implementasjonsdetaljer.

## Ikke gjør det for lett
Ikke erstatt portene med håndskrevne fake-implementasjoner i denne øvelsen; poenget er å bli fortrolig med begge mocking-rammeverkene. Ikke bruk `any()` overalt eller verifiser hvert eneste getter-kall. En god test låser kontrakten, ikke den interne linjerekkefølgen.

Ikke kopier den ferdige Mockito-klassen og bare bytt importene. Skriv MockK-suiten på nytt slik at oppsett, stubbing og verifisering faktisk repeteres.

## Formål i intervjuet
Caset lar deg forklare hvordan du isolerer en application service, hva som bør mockes, og hvorfor rene domeneberegninger normalt ikke trenger mocks. Du får også demonstrert at en test ikke bare skal bli grønn, men feile presist når en forretningsregel endres.

## Intervjuspørsmål / debrief
1. Hva er forskjellen på stubbing og verifisering?
2. Når foretrekker du en fake fremfor en mock?
3. Hvilke forskjeller merket du mellom Mockito og MockK i Kotlin?
4. Hvorfor kan for mange `verify`-kall gjøre refaktorering vanskelig?
5. Hva betyr red–green–refactor konkret i TODO 6?
6. Hvordan vet du at en test feiler av riktig grunn?
7. Hva ville du testet uten mocks dersom poengberegningen ble flyttet til en egen domenepolicy?

## Kommandoer

```bash
./mvnw test -pl case-41-unit-testing-mocks-tdd
./mvnw verify -pl case-41-unit-testing-mocks-tdd
```

Før du lager den første testen, er `BUILD SUCCESS` med null kjørte tester forventet. Etterpå skal Surefire rapportere testene du selv har opprettet.
