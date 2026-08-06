# Case 37 – Kotlin mapping, sealed types og collections

## Domene
Konsulent- og oppdragsmatching.

## Tid
35–45 minutter.

## Hva dette trener
Nullable typer, data classes, sealed interface, uttømmende `when`, input→domene→DTO-mapping, `mapNotNull`, filtrering, sammensatt sortering og gruppering.

## Scenario
Et importformat beskriver konsulenter med løse statuskoder og nullable felt. Oversett formatet til en trygg domenemodell, og bygg en offentlig kandidatliste.

## Oppgave og foreslått arbeidsrekkefølge
1. Oversett statuskode til `Availability`.
2. Map gyldig input til `Consultant`; ugyldige rader filtreres bort.
3. Map domenet til DTO med uttømmende `when`.
4. Map en liste med `mapNotNull`.
5. Filtrer og sorter med erfaring desc og navn asc.
6. Gruppér sertifiserte ferdigheter til en popularitetsrapport.

## Akseptansekriterier
- Blank/manglende navn og negative erfaringsår filtreres bort.
- Nullable skills behandles som tom liste.
- Alle sealed-varianter håndteres.
- Tom input gir tomt resultat.
- Ingen mutable collections, `!!`, Spring eller database.

## Ikke gjør det for lett
Ikke la inputmodellen lekke til DTO-en, og ikke bruk to uavhengige sorteringer når kontrakten krever én sammensatt comparator.

## Intervjuspørsmål / debrief
1. Når velger du `mapNotNull` fremfor `filter` + `map`?
2. Hvorfor passer sealed bedre enn løse strenger?
3. Når trenger du `sortedWith`?
4. Hvordan hjelper uttømmende `when` ved utvidelser?
5. Hvilke mellomtyper har datastrømmen?

## Kommando
`./mvnw test -pl case-37-kotlin-mapping-sealed-collections`
