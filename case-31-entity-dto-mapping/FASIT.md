# Fasit – Case 31

## Oppgaver og kontrakt

Løsningen implementerer fullt navn, inklusiv ansettelsesperiode, audience-styrt
DTO-mapping, listemapping, filtrering av aktive ansatte og aggregerte
avdelingssammendrag. Sensitive entitetsfelter finnes ikke i DTO-modellen.
E-post eksponeres internt og til leder, mens lønn bare eksponeres til leder.

Sertifiserte ferdigheter på en ansatt sorteres på erfaring synkende og navn
stigende. Avdelinger sorteres på navn; toppferdigheter rangeres på antall
forekomster synkende og navn stigende. Negativ `topSkillCount` avvises.

## Valgt løsning og Kotlin-konsepter

`fullName` er en extension property, og `isActiveOn` er en expression-body
extension. En `EmployeeEntity.toDto` samler grensemappingen. Små private
properties på `Audience` uttrykker feltpolicyen uten å duplisere hele mapperen.

Lister håndteres med `map` og `filter + map`. Aggregeringen bruker `groupBy`,
`flatMap`, `groupingBy().eachCount()`, eksplisitte comparators og `take`. Ingen
for-løkker, mutable collections eller `!!` er nødvendig.

## Edge cases og teststrategi

De eksisterende testene dekker alle audiences, nullable e-post, sensitive
feltgrenser, start- og sluttdato, sortering, tomme ferdighets- og ansattlister,
aktive ansatte, avdelingsaggregering og begrensning av toppferdigheter. En
tilleggstest dokumenterer at negativ grense avvises også ved tom input.

## Designvalg, alternativer og trade-offs

Audience-policyen ligger ved API-mappingen fordi regelen gjelder eksponering,
ikke om lønn eller e-post finnes i domenet. Dersom samme autorisasjonsregel
skulle brukes på flere API-er, burde den flyttes til en eksplisitt policy med
brukerkontekst og testes separat.

`filter + map` er valgt for aktive ansatte fordi filtreringsregelen er et eget,
navngitt steg. `mapNotNull` kunne gjort én passering, men ville blandet
filtrering og mapping og gjort intensjonen mindre tydelig. En dedikert mapper-
klasse kunne vært riktig med avhengigheter eller mange DTO-varianter; stateless
extensions er enklere her.

## Kort intervjuforklaring

«Entiteten representerer persistensbehov, DTO-en representerer en kontrollert
API-kontrakt. Mapperen er den eksplisitte grensen: den skjuler sensitive felt,
anvender audience-policyen og bruker collections-operasjoner til å bevare
rekkefølge og gjøre aggregeringen etterprøvbar.»
