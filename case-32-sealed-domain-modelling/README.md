# Case 32 - Sealed hierarkier som domenemodell

## Domene
Varsling / notifikasjoner

## Tid
45-60 minutter

## Hva dette trener
Læringsmål for dette caset:
- `sealed interface` og `sealed class` — når du velger hva, og hvorfor ikke `enum` eller `open class`
- Uttømmende `when` **uten** `else`, slik at kompilatoren fanger nye varianter
- `when` som uttrykk med expression body i stedet for `if/else`-kjeder
- `data object` for varianter uten data, og `data class` for varianter med data
- Felles egenskaper i sealed-grensesnittet (`val recipientId`, `val channel`) og smart casts
- `filterIsInstance`, `count { }` og `minOfOrNull` på lister av sealed-varianter
- Nullable returverdi (`RejectionReason?`) som modellering av "ingen grunn"

## Scenario
Et varslingssystem skal sende ulike typer varsler (`Notification`) på ulike kanaler (`Channel`), og hvert forsøk ender i et `DeliveryResult`. Typene er modellert som tre sealed hierarkier, men **all logikk mangler**: kanalvalg, prioritet, avvisningsårsak, beskrivelse og oppsummering er `TODO()`.

Reglene er ekte-verden-aktige: passordvarsler skal aldri havne i appen brukeren er utestengt fra, betalingsfeil skal ut på alt du har, og markedsføring krever samtykke.

## Oppgave
Implementer reglene som `when`-uttrykk over de sealed hierarkiene, og få testene grønne. Ingen `else`-gren der `when` kan være uttømmende.

## TODO / fokusområder
- TODO 1: `Channel.label` som extension property: `Email` → `"e-post"`, `Sms` → `"SMS"`, `InApp` → `"app"`.
- TODO 2: `NotificationPolicy.channelsFor(notification, recipient)` — rekkefølgen i listen er en del av kontrakten:
 - `PasswordReset` → `[Email]` hvis e-post finnes, ellers tom liste (ingen `InApp`)
 - `PaymentFailed` → `[Sms, Email, InApp]` av det mottakeren faktisk har; `InApp` alltid
 - `OrderShipped` → `[Email, InApp]`; `Email` bare hvis den finnes; aldri SMS
 - `MarketingCampaign` → `[Email]` bare med både samtykke og e-post, ellers tom liste
- TODO 3: `priorityOf(notification)` — `PasswordReset`/`PaymentFailed` → `HIGH`, `OrderShipped` → `NORMAL`, `MarketingCampaign` → `LOW`.
- TODO 4: `rejectionReasonFor(notification, recipient)` — `null` når minst én kanal finnes, `MARKETING_CONSENT_MISSING` ved manglende samtykke, ellers `MISSING_CONTACT_INFO`. Ikke dupliser kanalreglene.
- TODO 5: `describe(result)` — uttømmende `when` over `DeliveryResult` med smart casts og string templates.
- TODO 6: `List<DeliveryResult>.toReport()` — tellere per variant og korteste `retryAfter` (`null` hvis ingen kan prøves igjen).
- TODO 7: `List<Notification>.highestPriorityFirst()` — stabil sortering på prioritet.
- TODO 8: Legg til en ny variant (f.eks. `Notification.InvoiceOverdue`) *lokalt* og se hvilke `when`-uttrykk kompilatoren klager på. Fjern den igjen før du er ferdig — poenget er å oppleve gevinsten ved uttømmende `when`.

## Akseptansekriterier
- Alle tester i `NotificationDomainTest` er grønne.
- Ingen `else`-gren i `when` over `Notification`, `Channel` eller `DeliveryResult`.
- Ingen `is`-sjekk-kjeder med `!!` eller manuelle cast — bruk smart casts.
- Ingen `IllegalStateException("ukjent type")`-fallback noe sted.
- Kanalreglene finnes på ett sted; `rejectionReasonFor` gjenbruker dem i stedet for å kopiere dem.

## Formål i treningen
"Når bruker du sealed class fremfor enum?" er et av de vanligste Kotlin-spørsmålene for seniorutviklere. Poenget er ikke definisjonen, men hva du får: lukket variantsett, data per variant, og en kompilator som finner alle stedene du må oppdatere når domenet vokser. Dette caset lar deg vise det i praksis — og forklare hvorfor `else` i et `when` over et sealed hierarki ofte er en fremtidig bug.

## Ikke gjør det for lett
Ikke gjør hierarkiet flatt ved å legge alt i én `data class` med en `type`-enum og nullable felter. Ikke legg inn `else -> throw` for å slippe unna kompilatoren, og ikke gjør `channelsFor` til en `if`-kjede over `is`-sjekker når `when` er riktig verktøy.

## Treningsspørsmål / debrief
1. `sealed class` vs. `sealed interface` vs. `enum class` — når velger du hva?
2. Hva er den konkrete gevinsten ved at `when` er uttømmende, og hva mister du med `else`?
3. Hvorfor `data object` i stedet for `object` eller `data class` uten felter?
4. Hva skjer med et sealed hierarki når det skal serialiseres til JSON, og hvordan håndterer du det?
5. Hvor ville du plassert `NotificationPolicy` i en lagdelt Spring-applikasjon — og hvorfor er det viktig at den er fri for rammeverk?
6. Når er polymorfi på varianten (metode i grensesnittet) bedre enn `when` i en policy-funksjon?

## Kommandoer

```bash
./mvnw test -pl case-32-sealed-domain-modelling
```
