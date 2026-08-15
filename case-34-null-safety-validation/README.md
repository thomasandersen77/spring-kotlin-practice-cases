# Case 34 - Null-safety og validering

## Domene
Selvbetjent kunderegistrering

## Tid
50-70 minutter

## Hva dette trener
Læringsmål for dette caset:
- Null-safety i praksis: `?.`, `?:`, `let`, `takeIf`, `requireNotNull` — og hvorfor `!!` aldri er svaret
- Skillet mellom "nullable fordi input er upålitelig" og "nullable fordi feltet er valgfritt"
- `runCatching { }.getOrNull()` som alternativ til try/catch rundt parsing
- Feilakkumulering: samle alle feil i én runde i stedet for fail-fast
- Sealed hierarki (`ValidationResult`) med generics og `Nothing` som type for feilgrenen
- `buildList`, `mapNotNull`, `flatMap` og `filterIsInstance` på resultatlister
- Normalisering (trim, lowercase, uppercase, fjerning av whitespace) som del av mappingen inn til domenet

## Scenario
Et registreringsskjema kommer inn fra web som `RegistrationForm`: alt er nullable, alt kan være blankt, og `birthDate` er en streng. Domenet skal ha en `Customer` der bare de virkelig valgfrie feltene (`phone`, `referralCode`) fortsatt kan være null.

Brukeren skal få **alle** feilene sine på én gang — ikke én per innsending. `RegistrationValidator.validate` og hjelpefunksjonene er uløste (`TODO()`), og testene beskriver kontrakten.

## Oppgave
Implementer validering og normalisering fra `RegistrationForm` til `Customer` med `ValidationResult`. Ingen exceptions for forventet ugyldig input, ingen `!!`.

## Valideringsregler (eksakte feilmeldinger)
- `email`: påkrevd. Blank/mangler → `"E-post er påkrevd"`. Må ha ett `@`, ikke-tom lokaldel og et domene med punktum → ellers `"E-post er ugyldig"`. Normaliseres til trimmet lowercase.
- `fullName`: påkrevd. Blank/mangler → `"Navn er påkrevd"`. Mindre enn 2 tegn etter trim → `"Navn må ha minst 2 tegn"`. Normaliseres: trimmet, og gjentatt whitespace inne i navnet komprimeres til ett mellomrom.
- `birthDate`: påkrevd. Blank/mangler → `"Fødselsdato er påkrevd"`. Ikke parsebar som `yyyy-MM-dd` → `"Fødselsdato må være på formatet yyyy-MM-dd"`. Under 18 år på `today` → `"Kunden må være minst 18 år"`.
- `postalCode`: påkrevd. Blank/mangler → `"Postnummer er påkrevd"`. Ikke nøyaktig fire siffer → `"Postnummer må være fire siffer"`.
- `phone`: valgfri. Blank/mangler → `null` i domenet (ingen feil). Oppgitt: whitespace fjernes, og resultatet må være åtte siffer → ellers `"Telefonnummer må være åtte siffer"`.
- `marketingConsent`: `null` betyr `false`.
- `referralCode`: valgfri. Blank/mangler → `null`. Ellers trimmet og i store bokstaver.
- Feil: maks **én** feil per felt (første regel som slår til), og feillisten sorteres på feltnavn asc.

## TODO / fokusområder
- TODO 1: `String?.normalizedOrNull()` — trimmet verdi eller `null`, som én expression body.
- TODO 2: `String?.toLocalDateOrNull()` — trygg parsing med `runCatching`.
- TODO 3: `RegistrationValidator.validate(form)` — valider alle feltene, samle feilene, og bygg `Customer` bare når alt er gyldig.
- TODO 4: `List<RegistrationForm>.validCustomers(validator)` med `mapNotNull`.
- TODO 5: `List<RegistrationForm>.allErrors(validator)` med `flatMap` + `filterIsInstance`.
- TODO 6: Vurder om validering per felt bør trekkes ut i små private funksjoner som returnerer `Pair<T?, FieldError?>`, `ValidationResult<T>` eller noe enklere. Velg én tilnærming og begrunn den — dette er casets viktigste designvalg.
- TODO 7: Vær klar til å forklare hvorfor `ValidationResult.Invalid` kan være `ValidationResult<Nothing>`.

## Akseptansekriterier
- Alle tester i `RegistrationValidatorTest` er grønne.
- Ingen `!!`, ingen `lateinit`, ingen `try`/`catch` rundt datoparsing.
- Ingen exception kastes for ugyldig brukerinput — bare `ValidationResult.Invalid`.
- Alle feil kommer i samme resultat; validatoren er ikke fail-fast.
- `Customer` har ikke nullable felter utover `phone` og `referralCode`.
- Ingen `var` eller mutable lister i det offentlige API-et (en lokal `buildList` er greit).

## Formål i treningen
Null-safety er Kotlins mest åpenbare salgsargument, men i treningen handler det om noe mer: hvor konverterer du utrygg input til trygge typer, og hvordan rapporterer du feil? Kandidater som strør `!!` og `try/catch` rundt seg avslører seg raskt. Kandidater som viser en tydelig grense — utrygg `RegistrationForm` inn, validert `Customer` ut — og som kan begrunne fail-fast vs. feilakkumulering, ser ut som folk som har vedlikeholdt produksjonskode.

## Ikke gjør det for lett
Ikke fail-fast med `require`/`throw` på første feil — hele poenget er at brukeren skal se alt som er galt. Ikke gjør feltene i `Customer` nullable for å slippe validering, og ikke bruk et valideringsrammeverk (Bean Validation); poenget er språket.

## Treningsspørsmål / debrief
1. Når er fail-fast riktig, og når må du akkumulere feil? Hvordan påvirker det API-design?
2. `ValidationResult` vs. `Result<T>` vs. exceptions vs. `Either` — hva velger du i en Spring-applikasjon, og hvorfor?
3. Hvorfor er `Invalid : ValidationResult<Nothing>` mulig og nyttig?
4. Hvor i lagene hører denne valideringen hjemme, og hva hører hjemme som invariant i domenetypen selv?
5. Hva er forskjellen på `?.let { }` og `if (x != null)` — og når gir smart cast deg det samme gratis?
6. Hvordan ville du testet dette med parameteriserte tester i stedet for én test per regel?

## Kommandoer

```bash
./mvnw test -pl case-34-null-safety-validation
```
