# Case 39 – Spring Boot-integrasjon med WireMock

## Scenario og tid
Et internt API henter konsulentkapasitet fra en ekstern leverandør med egne statuskoder og transportmodeller. Tidsboks: 60–75 minutter.

## Læringsmål og avhengighetsretning
`Controller → CapacityService → CapacityPort ← RestClientCapacityAdapter → leverandør`. Tren ekstern DTO, intern domenemodell, stabil response-DTO, anti-corruption layer, feilsemantikk og WireMock på dynamisk port.

ACL-en er ikke bare mekanisk mapping: den beskytter ordene og betydningen i vårt domene mot leverandørens statuskoder.

## TODO-er
1. Oversett ekstern status med uttømmende `when`.
2. Map perioder og ferdigheter til intern modell.
3. Gjør HTTP-kall med `RestClient` og nødvendig header.
4. Skill 404, 5xx/timeout og ugyldig payload.
5. Orkestrer gjennom porten i service.
6. Map domenet til response-DTO.
7. Eksponer stabil controller-kontrakt.

## Feilsemantikk og tester
Ekstern 404 → `ConsultantNotFound`; timeout/5xx → `ProviderUnavailable`; ugyldig JSON/status → `InvalidProviderResponse`. Test ren mapping, service uten Spring og ekte adapter mot WireMock for 200, 404, 500, treg respons, ugyldig JSON, ukjent status og korrekt path/header.

Caset er bevisst uten H2: persistens gir ikke et tydelig læringspoeng og ville overskygge WireMock.

## Ikke gjør det for lett
Ikke returner ekstern DTO fra controlleren, ikke la eksterne statuskoder lekke ut, og ikke bruk tilfeldige `sleep` i tester.

## Debrief
Hvorfor eier application-siden porten? Hva beskytter ACL-en? Hva skiller 404 fra 500? Hva kan retries? Hva overvåkes? Når er en enkel mapper nok?

## Kommando
`mvn test -pl case-39-wiremock-external-integration`
