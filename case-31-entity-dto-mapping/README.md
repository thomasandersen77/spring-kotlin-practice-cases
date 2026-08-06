# Case 31 - Entitet til DTO-mapping

## Domene
HR / ansattregister

## Tid
45-60 minutter

## Hva dette trener
Læringsmål for dette caset:
- Extension functions og extension properties som mapping-verktøy (`EmployeeEntity.toDto()`)
- Mapping av lister: `map`, `filter` + `map` og `mapNotNull` — og når du velger hva
- Delvis mapping: at ikke alle felter skal med, og at feltutvalget kan avhenge av kontekst (audience)
- Nullable felter i entitet vs. DTO (`?.`, `?:`, `takeIf`)
- Aggregert mapping med `groupBy`, `mapValues`, `flatMap`, `groupingBy().eachCount()` og `sortedWith`
- Hvorfor mapping hører hjemme på grensen mellom lag, ikke inne i domenelogikken

## Scenario
`EmployeeEntity` er persistensmodellen i et HR-system: mange felter, noen nullable, og noen som aldri skal ut av huset (`nationalIdentityNumber`, `internalNotes`). API-et skal levere `EmployeeDto` — færre felter, andre navn, og innhold som avhenger av hvem som spør: en anonym søker (`PUBLIC`), en kollega (`INTERNAL`) eller en leder (`MANAGER`).

Alle mapping-funksjonene i `EmployeeMapping.kt` er uløste (`TODO()`). Testene er røde og beskriver kontrakten.

## Oppgave
Implementer mappingen fra entitet til DTO som extension functions, og få testene grønne. Feltutvalget styres av `Audience`, og aggregeringen per avdeling skal bygges med collections-API-et — ikke med for-løkker og mutable lister.

## TODO / fokusområder
- TODO 1: `EmployeeEntity.fullName` som extension property: `"$firstName $lastName"`.
- TODO 2: `EmployeeEntity.isActiveOn(date)` med expression body: aktiv når `employmentStart <= date` og (`employmentEnd == null` eller `employmentEnd >= date`).
- TODO 3: `EmployeeEntity.toDto(audience, today)` — `nationalIdentityNumber`, `phone` og `internalNotes` skal aldri ut; `email` bare for `INTERNAL`/`MANAGER`; `monthlySalaryNok` bare for `MANAGER`; `certifiedSkills` = navn på sertifiserte skills sortert på erfaring desc, deretter navn asc.
- TODO 4: `List<EmployeeEntity>.toDtos(...)` — list-mapping uten mutable liste, rekkefølgen bevares.
- TODO 5: `List<EmployeeEntity>.toActiveDtos(...)` — filtrer bort ansatte som ikke er aktive på `today`. Velg bevisst mellom `filter { }.map { }` og `mapNotNull { }`, og vær klar til å begrunne valget.
- TODO 6: `List<EmployeeEntity>.toDepartmentSummaries(today, topSkillCount)` — én rad per avdelingsnavn sortert asc, antall ansatte, antall aktive, og de mest utbredte sertifiserte skills (antall desc, deretter navn asc).
- TODO 7: Vurder om `Audience`-regelen hører hjemme i mapping-funksjonen eller i en egen policy-funksjon. Skriv én setning i debrief om hvorfor du valgte som du gjorde.

## Akseptansekriterier
- Alle tester i `EmployeeMappingTest` er grønne.
- Sensitive felter (`nationalIdentityNumber`, `internalNotes`, `phone`) finnes ikke i DTO-en for noen audience.
- Ingen `for`-løkker, ingen mutable collections og ingen `!!` i mapping-koden.
- Mappingen er samlet på ett sted; entitetene har ingen kunnskap om DTO-ene ut over extension-funksjonene.
- Tomme lister gir tomme resultater — ikke null og ingen exception.

## Formål i intervjuet
Entitet-til-DTO-mapping er noe av det mest hverdagslige du gjør i Kotlin/Spring — og derfor et takknemlig sted for intervjueren å se om du *tenker* i Kotlin. Gjør du det med extension functions og collections-API, eller med en `MapperUtil`-klasse full av statiske metoder og for-løkker? Kan du forklare hvorfor DTO-en ikke bare er en kopi av entiteten, og hvem som eier reglene for hva som eksponeres?

## Ikke gjør det for lett
Ikke lag én `toDto()` per audience med copy-paste-kode, og ikke løs feltmaskeringen ved å sende hele entiteten ut og filtrere i controlleren. Ikke bruk refleksjon eller et mapping-bibliotek — poenget er språket, ikke rammeverket.

## Intervjuspørsmål / debrief
1. Hvorfor extension function fremfor en metode på entiteten eller en egen `Mapper`-klasse? Hva er trade-offen?
2. Når velger du `mapNotNull` fremfor `filter { }.map { }` — og hva skjer med lesbarheten?
3. Hvor bør regelen "lønn er bare for ledere" ligge: i mapperen, i domenet eller i API-laget?
4. Hva er faren ved at DTO-en speiler entiteten 1:1, og hvordan slår det ut når databasen endres?
5. Hvordan ville du testet at et sensitivt felt aldri lekker ut i JSON-responsen?

## Kommandoer

```bash
mvn test -pl case-31-entity-dto-mapping
```
