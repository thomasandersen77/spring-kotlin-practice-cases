# Case 38 – Basic Spring Boot REST med H2

## Domene og scenario
Et lite lager-API skal registrere produkter, hente ett produkt, liste aktive produkter og endre beholdning uten at den blir negativ.

## Tid
60 minutter.

## Læringsmål
HTTP request → tynn controller → application service → Spring Data JPA → H2 → response DTO, Bean Validation, mapping, transaksjonsgrense og sentral feiloversettelse.

## API-kontrakt
- `POST /api/products` oppretter og gir 201.
- `GET /api/products/{id}` gir 200 eller 404.
- `GET /api/products` gir aktive produkter sortert på navn.
- `PATCH /api/products/{id}/stock` endrer beholdning; negativ sluttbeholdning gir 409.
- Ugyldig request gir 400.

## TODO-er og arbeidsrekkefølge
1. Beskytt beholdningsinvarianten i modellen.
2. Map request til entity og entity til response.
3. Opprett og lagre i service med `@Transactional`.
4. Hent én ressurs eller kast `ProductNotFound`.
5. List aktive produkter via repository-query.
6. Endre beholdning i en transaksjon.
7. Fullfør HTTP-feiloversettelsen.

## Akseptansekriterier og teststrategi
Rene domenetester, `@DataJpaTest` for lagring/query og MockMvc for 201, 400, 404, 409, JSON og databaseeffekt. `open-in-view=false`; H2 bruker `create-drop` i trening.

## Avgrensninger
Ingen ekstra undermoduler, porter eller abstraksjoner uten tydelig verdi. H2 kan skjule PostgreSQL-forskjeller i SQL, låsing og typer.

## Ikke gjør det for lett
Ikke legg regler i controlleren, returner JPA-entiteten eller behold en åpen persistence context gjennom web-laget.

## Debrief
Hvorfor tynn controller? Hvor ligger `@Transactional`? Hva skiller DTO, domene og entity? Hvorfor `open-in-view=false`? Hva ville du endret i produksjon?

## Kommando
`./mvnw test -pl case-38-basic-spring-h2-rest`
