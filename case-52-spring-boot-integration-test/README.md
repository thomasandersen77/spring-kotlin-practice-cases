# Case 52 – Spring Boot integration test

## Mål

Skriv en Spring Boot-integrasjonstest fra en nesten blank testfil. Produksjonskoden er ferdig. Oppgaven er å bevise HTTP-kontrakten og databaseeffektene gjennom hele Spring-stacken.

**Tidsboks:** 30–45 minutter.

## Scenario

Et enkelt lager-API håndterer reservasjoner:

```http
POST /api/reservations
GET  /api/reservations/{id}
```

En reservasjon inneholder:

- `productCode`
- `quantity`
- `customerEmail`

Forretningsregler:

- `quantity` må være større enn null.
- `productCode` må finnes.
- Det kan ikke reserveres flere varer enn tilgjengelig beholdning.
- En vellykket reservasjon lagres og reduserer beholdningen i samme transaksjon.

## Oppgave

Skriv testene i:

```text
src/test/kotlin/com/interview/case52/reservations/ReservationIntegrationTest.kt
```

Konstruer testklassen selv. Den skal bruke:

- `@SpringBootTest`
- `@AutoConfigureMockMvc`
- injisert `MockMvc`
- injisert `ObjectMapper`
- `ProductRepository` og `ReservationRepository` for testoppsett og etterkontroll
- eksplisitt databaserydding mellom testene

Ikke mock noen Spring-beans. Ikke bruk `@Transactional` på testklassen.

### Test 1 – vellykket reservasjon

1. Lagre et produkt med `productCode = "KOTLIN-21"` og beholdning 10.
2. Send `POST /api/reservations` med antall 3 og en gyldig e-postadresse.
3. Verifiser `201 Created`.
4. Verifiser responsens sentrale JSON-felter.
5. Les produktet på nytt fra repository og verifiser beholdning 7.
6. Verifiser at én reservasjon er lagret med riktige verdier.

### Test 2 – utilstrekkelig beholdning

1. Lagre et produkt med beholdning 2.
2. Forsøk å reservere 3.
3. Verifiser `409 Conflict`.
4. Verifiser at ingen reservasjon er lagret.
5. Les produktet på nytt og verifiser at beholdningen fremdeles er 2.

### Test 3 – ugyldig request

1. Lagre et produkt med beholdning 10.
2. Send en request med `quantity = 0`.
3. Verifiser `400 Bad Request`.
4. Verifiser at databasen ikke er endret.

## Akseptansekriterier

- Alle tre scenarioene testes gjennom `MockMvc` og den virkelige Spring-konteksten.
- Testene verifiserer både HTTP-respons og persistert tilstand.
- Testene er uavhengige og kan kjøres i vilkårlig rekkefølge.
- Testnavnene beskriver observerbar oppførsel.
- Hele modulen er grønn.

## Kommandoer

Fra repository-roten:

```bash
./mvnw test -pl case-52-spring-boot-integration-test
./mvnw verify -pl case-52-spring-boot-integration-test
```

## Muntlig debrief

Vær forberedt på å forklare:

1. Hvorfor dette er en integrasjonstest og ikke en enhetstest.
2. Hva `@SpringBootTest` og `@AutoConfigureMockMvc` gjør.
3. Hvorfor `MockMvc` ikke starter en virkelig HTTP-server.
4. Hvorfor HTTP-responsen alene ikke beviser atomisk persistens.
5. Hvordan testene isoleres fra hverandre.
6. Hvorfor `@Transactional` på testklassen kan skjule den faktiske commit-grensen.
7. Forskjellen på `@SpringBootTest`, `@WebMvcTest` og `@DataJpaTest`.
8. Hva H2 kan skjule sammenlignet med PostgreSQL og Testcontainers.

