# Case 26 - Kotlin idioms drill

## Domene
Konsulentdata / rapportering

## Tid
30-45 minutter

## Hva dette trener
- Kotlin-idiomer (collections, null-safety, `when`)
- Extension functions
- Refaktorering med tester som sikkerhetsnett
- Scope functions (`let`, `apply`, `also`, `run`)

## Scenario
`ConsultantReports.kt` inneholder fungerende, men bevisst klønete kode som lager rapporter over konsulenter, timepriser og ferdigheter — skrevet som "Java med Kotlin-syntaks". Testene er grønne. Din jobb er å refaktorere til idiomatisk Kotlin og holde testene grønne hele veien.

## Oppgave
Skriv om Java-aktig Kotlin til idiomatisk Kotlin uten å endre oppførsel. Bruk collections-API-et, null-safety og uttrykksbasert kode der det gjør koden kortere OG mer lesbar.

## TODO / fokusområder
- TODO: Erstatt `for`-løkker + mutable lister med `filter`, `map`, `sortedBy`, `groupBy`, `associateBy`, `sumOf` og `fold`.
- TODO: Fjern null-sjekk-pyramidene med `?.`, `?:`, `let` og smart casts. Vurder hvor `!!` aldri hører hjemme.
- TODO: Skriv om `buildSkillIndex` til én expression-body-funksjon.
- TODO: Innfør en extension function (f.eks. `List<Consultant>.availableIn(city: String)`) der det gir bedre lesbarhet.
- TODO: Bruk `when` som uttrykk i `seniorityLabel` i stedet for if/else-kjeden.
- TODO: Erstatt string-konkatinering med string templates og `joinToString`.

## Akseptansekriterier
- Alle eksisterende tester er fortsatt grønne etter refaktorering.
- Ingen `var` eller mutable collections i offentlig API.
- Ingen `!!`.
- Koden er kortere og mer lesbar — ikke bare "smartere".

## Formål i intervjuet
I live-koding er overgangen Java → Kotlin det intervjueren ser etter. En vanlig felle for erfarne Java-utviklere er å skrive "Java med Kotlin-syntaks": `for`-løkker med mutable lister, null-sjekk-pyramider og manuelle map-oppbygginger. Det som skiller "kan Kotlin" fra "tenker i Kotlin" er hvor naturlig du bruker collections-API-et, null-safety og uttrykksbasert kode.

## Ikke gjør det for lett
Ikke skriv om til én kjede av femten operasjoner ingen forstår. Idiomatisk betyr lesbart — ikke maksimalt kompakt.

## Intervjuspørsmål / debrief
1. Når velger du en `for`-løkke fremfor en kjede av collection-operasjoner (ytelse/lesbarhet)?
2. Hva er forskjellen på `let` og `run`, og når skaper scope functions mer forvirring enn verdi?
3. Hvorfor er `List` i Kotlin read-only, men ikke immutable — og når betyr forskjellen noe?
4. Når bruker du `sequence` i stedet for vanlige collection-operasjoner?

## Kommandoer

```bash
./mvnw test -pl case-26-kotlin-idioms-drill
```
