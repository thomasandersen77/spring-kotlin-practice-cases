# Case 29 - LLM bak port/adapter med modell-fallback

## Domene
AI-integrasjon / konsulentmatching

## Tid
45-60 minutter

## Hva dette trener
- Ports/adapters mot ekstern AI-leverandør
- Fallback-design (primær → sekundær modell)
- Feilsemantikk (kapasitetsfeil vs. parsefeil)
- Testing med fakes uten nettverkskall

## Scenario
`CvScoringService` skal score en CV mot en prosjektforespørsel ved hjelp av en LLM. Porten `LlmClient` finnes, men tjenesten kaller i dag primærmodellen direkte uten feilhåndtering: ett 503-svar velter hele scoringen. Kravene er:
- Primærmodellen prøves først.
- Ved `LlmOverloadedException` (503) skal fallback-modellen brukes automatisk.
- Ved `LlmInvalidResponseException` (ugyldig/uparsbar respons) skal det IKKE falles tilbake — det er en feil hos oss (prompt/parsing), ikke kapasitet. Feilen skal propagere.
- Resultatet skal si hvilken modell som faktisk ble brukt (for sporbarhet/audit).

## Oppgave
Integrer en upålitelig LLM-leverandør bak en ren domeneport: fallback fra primær- til sekundærmodell, presis feilsemantikk og testbarhet uten nettverkskall.

## TODO / fokusområder
- TODO: Implementer fallback-logikken i `CvScoringService` — testene beskriver kontrakten.
- TODO: Valider `Score` som value object (0–100) slik at en hallusinert verdi fra LLM-en aldri blir et gyldig domeneobjekt (jf. case-01: valider nær konstruktøren).
- TODO: Vurder én retry mot primærmodellen før fallback. Hva er trade-offen (latens vs. kvalitet på svar fra en svakere modell)?
- TODO: Diskuter hvor prompten hører hjemme: domene, application eller adapter? (Hint: prompten koder forretningsregler, HTTP-detaljene gjør det ikke.)
- TODO: Hvordan ville du testet selve adapteren (den som snakker HTTP mot Gemini/OpenAI)? Wiremock/kontraktstester — og hvorfor de ikke hører hjemme i denne modulen.

## Akseptansekriterier
- Alle tester grønne: fallback ved 503, propagering ved ugyldig respons, korrekt modell i resultatet.
- `CvScoringService` har ingen kjennskap til HTTP, JSON eller leverandørspesifikke detaljer.
- Ugyldig score fra LLM stoppes ved konstruksjon av `Score`.
- Du kan tegne opp lagdelingen (domene → port → adapter) og forklare hva som byttes ut når leverandøren endres fra Gemini til OpenAI.

## Formål i treningen
Dette er et godt case for å trene på å forklare arkitekturvalg høyt: "Jeg bygde LLM-integrasjon med Clean Architecture — slik holder jeg OpenAI/Gemini-detaljene ute av domenet." Poenget her er ikke AI — det er arkitektur: ports & adapters, feilhåndtering som domenebeslutning, og at alt kan testes med fakes uten API-nøkler. Klarer du å forklare hvorfor `LlmClient`-porten ikke vet noe om HTTP eller modellnavn, viser du nettopp den lagdelingsforståelsen treningen sikter mot.

## Ikke gjør det for lett
Ikke fang alle exceptions og prøv fallback uansett. Forskjellen på kapasitetsfeil (503) og vår egen parsefeil er selve poenget i caset.

## Treningsspørsmål / debrief
1. Hvorfor er dette en port/adapter og ikke bare "et interface"? Hva er forskjellen på ACL (case-21) og en port?
2. Hvor ville du lagt inn circuit breaker, og hvorfor er det en adapter-/infrastrukturbekymring?
3. Hvordan holder du LLM-svar deterministiske nok til å teste (temperatur, structured output, validering)?
4. Hva logger du ved fallback, og hvorfor er "hvilken modell svarte" viktig i produksjon?

## Kommandoer

```bash
./mvnw test -pl case-29-llm-port-adapter-fallback
```
