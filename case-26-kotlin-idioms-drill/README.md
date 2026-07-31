# Case 26 - Kotlin idioms drill

Ren syntaks- og idiomtrening: skriv om Java-aktig Kotlin til idiomatisk Kotlin uten å endre oppførsel.

## Hvorfor dette caset er viktig for deg
Du profilerer deg som Kotlin-utvikler med lang Java-bakgrunn, og i live-koding er det nettopp
overgangen Java → Kotlin intervjueren ser etter. En vanlig felle for erfarne Java-utviklere er å
skrive "Java med Kotlin-syntaks": `for`-løkker med mutable lister, `if (x != null)`-pyramider og
manuelle `HashMap`-oppbygginger. Sopra Steria kommer til å vurdere hvor naturlig du bruker
collections-API-et, null-safety og uttrykksbasert kode — det er dette som skiller "kan Kotlin"
fra "tenker i Kotlin". Dette caset finnes ikke blant case 1–25, som fokuserer på domenedesign;
her trener du ren fingerferdighet og syntaks under tidspress.

## Scenario
`ConsultantReports.kt` inneholder fungerende, men bevisst klønete kode som lager rapporter over
konsulenter, timepriser og ferdigheter. Testene er grønne. Din jobb er å refaktorere til
idiomatisk Kotlin og holde testene grønne hele veien.

## TODO / fokusområder
- TODO: Erstatt `for`-løkker + mutable lister med `filter`, `map`, `sortedBy`, `groupBy`, `associateBy`, `sumOf` og `fold`.
- TODO: Fjern null-sjekk-pyramidene med `?.`, `?:`, `let` og smart casts. Vurder hvor `!!` aldri hører hjemme.
- TODO: Skriv om `buildSkillIndex` til én expression-body-funksjon.
- TODO: Innfør en extension function (f.eks. `List<Consultant>.availableIn(city: String)`) der det gir bedre lesbarhet.
- TODO: Bruk `when` som uttrykk i `seniorityLabel` i stedet for if/else-kjeden.
- TODO: Vurder scope functions (`let`, `apply`, `also`, `run`) — og like viktig: hvor de IKKE bør brukes.
- TODO: Erstatt string-konkatinering med string templates og `joinToString`.

## Akseptansekriterier
- Alle eksisterende tester er fortsatt grønne etter refaktorering.
- Ingen `var` eller mutable collections i offentlig API.
- Ingen `!!`.
- Koden er kortere og mer lesbar — ikke bare "smartere".

## Debrief-spørsmål du bør kunne svare på
- Når velger du en `for`-løkke fremfor en kjede av collection-operasjoner (ytelse/lesbarhet)?
- Hva er forskjellen på `let` og `run`, og når skaper scope functions mer forvirring enn verdi?
- Hvorfor er `List` i Kotlin read-only, men ikke immutable — og når betyr forskjellen noe?
- Når bruker du `sequence` i stedet for vanlige collection-operasjoner?

## Hvordan kjøre
```bash
mvn test -pl case-26-kotlin-idioms-drill
```
