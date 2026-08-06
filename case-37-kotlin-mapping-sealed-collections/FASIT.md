# Fasit – Case 37

## Oppgaver og kontrakt

Løsningen oversetter eksterne statuskoder til et sealed domene, filtrerer
ugyldige inputrader, mapper domene til DTO, rangerer tilgjengelige konsulenter
og bygger popularitetsrapport for sertifiserte ferdigheter. Nullable
skill-lister blir tomme, blanke navn og negative erfaringsår filtreres bort.

`PARTIAL` tolkes som en reell delmengde av full tilgjengelighet og krever derfor
1–99 prosent. Manglende, 0 eller 100 prosent gir `Unknown("PARTIAL")` ved
importgrensen. `PartiallyAvailable` håndhever samme invariant selv.

## Valgt løsning og Kotlin-konsepter

`availabilityFrom` er et `when` over den løse inputkoden. DTO-mappingen bruker
et uttømmende `when` over `Availability`, slik at alle sealed-varianter får en
label og nye varianter gir compile-feil.

Inputmappingen bruker tidlige nullable-returer og normaliserer navn med trim.
Lister behandles med `mapNotNull`, `filter`, én sammensatt comparator,
`flatMap`, `groupingBy().eachCount()` og `map`. Ingen mutable collections eller
`!!` brukes.

Skill-popularitet betyr antall konsulenter med ferdigheten. Derfor brukes
`distinct` per konsulent før telling, slik at duplikate importlinjer ikke teller
samme konsulent flere ganger.

## Edge cases og teststrategi

De eksisterende testene dekker kjente og ukjente statuser, ugyldige rader,
nullable skills, sertifisering, ranking, tie-break og tom input. Tilleggstestene
dokumenterer prosenttolkningen, alle DTO-labeler og unik konsulenttelling ved
duplikate skills.

## Designvalg, alternativer og trade-offs

Ugyldig partial-prosent kunne vært behandlet som en avvist inputrad. `Unknown`
er valgt fordi resten av raden fortsatt er brukbar, samtidig som domenet ikke
later som statusen er gyldig. Et rikere importresultat med eksplisitte warnings
ville vært bedre i produksjon.

`mapNotNull` passer fordi filtrering og mapping er én samlet grenseoperasjon:
`toDomain` uttrykker om raden kan bli et domeneobjekt. I rankingen er
`filter + sortedWith + map` tydeligere fordi hvert steg har en separat hensikt.

## Kort intervjuforklaring

«Jeg oversetter løs ekstern data til et lukket sealed domene ved importgrensen.
Ugyldige rader blir null og filtreres med `mapNotNull`; gyldige objekter kan
deretter behandles uten nullable felt. Uttømmende `when` beskytter DTO-mappingen,
og én comparator uttrykker hele rankingkontrakten.»
