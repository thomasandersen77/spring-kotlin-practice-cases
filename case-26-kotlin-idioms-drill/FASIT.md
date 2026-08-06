# Fasit – Case 26

## Oppgaver og kontrakt

Oppgaven er en oppførselsbevarende refaktorering fra Java-preget Kotlin til
idiomatisk Kotlin. Rapportene skal fortsatt filtrere og sortere seniorer,
beskrive nullable lokasjon, bygge ferdighetsindeks, summere dagskostnad,
klassifisere senioritet og formatere byoversikten med samme rekkefølge og tekst
som før.

## Valgt løsning og Kotlin-konsepter

- `filter`, `sortedByDescending` og `map` erstatter midlertidige lister.
- `flatMap` lager én rad per ferdighet og konsulent, før `groupBy` bygger
  ferdighetsindeksen direkte som `Map<String, List<String>>`.
- Elvis-operator, nullable chaining og smart cast håndterer manglende eller
  blank lokasjon uten `!!`.
- `sumOf` uttrykker aggregeringen av dagskostnad.
- Et expression-body-`when` uttrykker de ordnede senioritetsgrensene.
- `availableIn` er en extension på `List<Consultant>`, og `joinToString`
  formaterer navnene uten manuell strengbygging.

Alle resultater er read-only typer i det offentlige API-et, og implementasjonen
bruker ingen `var` eller mutable collections.

## Edge cases og teststrategi

De eksisterende testene fungerer som regresjonstester for den avtalte
oppførselen. De dekker tom kostnadsliste, nullable og blank by, alle
senioritetsgrenser, negativ erfaring, flere ferdigheter og rekkefølgen i
sortering og sammendrag. Refaktoreringen introduserer ingen ny domenekontrakt,
så ytterligere tester er ikke nødvendige.

## Designvalg, alternativer og trade-offs

Vanlige collections er valgt fremfor `Sequence`: datasettene i et
rapportdrill er små, og eager mellomresultater gir enklere debugging. Ved svært
store lister og lange kjeder kunne en sekvens redusert allokeringer.

`groupBy` med `valueTransform` er brukt direkte fremfor en separat `mapValues`.
Begge er idiomatiske; denne varianten unngår å gruppere `Pair`-objektene i
sluttresultatet. En `for`-løkke kunne fortsatt vært riktig dersom én passering
og minimalt med allokering var viktigere enn det deklarative dataforløpet.

Scope functions er bevisst ikke brukt bare for korthet. Tidlige returer gjør
nullkontrakten i `describeLocation` mer lineær.

## Kort intervjuforklaring

«Jeg identifiserte først hvert dataforløp og erstattet mekanisk mutasjon med
operasjonen som beskriver hensikten. Jeg beholdt rekkefølge og feilsemantikk,
unngikk `!!`, og brukte lokale navn der en kompakt kjede ville blitt mindre
lesbar.»
