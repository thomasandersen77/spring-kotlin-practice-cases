# Fasit – Case 35

## Oppgaver og kontrakt

Løsningen implementerer hele søke-DSL-en, AND/OR/NOT-komposisjon,
predikatfabrikk, lazy konsulentindeks og deterministisk rapport. Kriterier på
toppnivå kombineres med AND. `anyOf` kombinerer interne kriterier med OR og
opptrer som ett AND-kriterium mot resten; en tom blokk matcher alle.

## Valgt løsning og Kotlin-konsepter

Et `ConsultantPredicate` er en funksjonstype. Builderen kapsler en privat
mutable liste av slike funksjoner; bare det ferdige predikatet lekker ut.
Kriteriefunksjonene lukker over parameterne sine. En lambda med
`ConsultantSearchBuilder` som receiver gir DSL-kall uten eksplisitt receiver.

`and` og `or` er short-circuiting infix extensions, og `not` er en operator.
`hasAllSkills` bruker at `all` på tom input er sann. `@DslMarker` forhindrer at
en ytre builder brukes implisitt fra en nested `anyOf`-blokk.

Indeksen har én delt `consultants by lazy(load)` og to egne lazy-avledninger.
Dermed kjøres `load` null ganger uten bruk og høyst én gang totalt. Rapporten
bruker `buildString`, sortering, string templates og `roundToInt`.

## Edge cases og teststrategi

Testene dekker tomt søk, tom OR-gruppe, alle kriterietyper, komposisjon og
negasjon, tom `vararg`, rekkefølge, ukjent ferdighet, lazy lasteteller,
sortering, avrunding og tom rapport uten sluttlinjeskift. De eksisterende
testene dekker dermed hele kontrakten.

## Designvalg, alternativer og trade-offs

Intern mutasjon er pragmatisk i en kortlivet builder og er fullstendig
innkapslet. En immutable builder ville krevd at alle DSL-kall returnerte en ny
builder og gjort lambda-with-receiver-syntaksen tyngre.

Et DSL er nyttig når kombinasjonene er mange og lesbarheten ved kallstedet har
verdi. For et fast sett enkle felt ville en `SearchCriteria`-data class vært
lettere å serialisere, validere og oversette til en databasequery. Denne DSL-en
evaluerer funksjoner i minnet; databasebruk ville krevd en uttrykkstre-
representasjon fremfor vilkårlige JVM-lambdas.

Standard `lazy` er synkronisert og egner seg når indeksen kan aksesseres fra
flere tråder. En annen `LazyThreadSafetyMode` kan redusere overhead dersom
eierskapet garantert er entrådet.

## Kort intervjuforklaring

«DSL-en samler små predikater og komponer dem etter tydelige boolske regler.
Lambda with receiver gjør kallstedet deklarativt, mens det ferdige resultatet
fortsatt bare er `(Consultant) -> Boolean`. Lazy-indeksen deler én lasting, og
rapporten har eksplisitt determinisme og tomtilfelle.»
