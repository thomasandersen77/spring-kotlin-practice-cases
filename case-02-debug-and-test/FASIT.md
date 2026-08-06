# Fasit – Case 02

## Oppgaver og kontrakt

Løsningen definerer perioden som halvåpen: `from` er inkludert og `to` er
ekskludert. Dermed gir `from == to` en tom periode og resultatet 0. `from > to`
er ugyldig input og avvises. Bare mandag–fredag teller, og en arbeidsdag som
finnes i `absenceDates` trekkes fra. Fravær på helg, på `to` eller utenfor
perioden har ingen effekt.

## Valgt løsning og Kotlin-konsepter

Datoene produseres lazily med `generateSequence` og avgrenses med `takeWhile`.
`count` uttrykker deretter domeneregelen direkte. En privat extension-funksjon
gir navnet `isWorkingDay` til helgeregelen. `require` plasserer valideringen ved
inngangen til domeneoperasjonen.

Controlleren er fortsatt en tynn oversettelse mellom request, domenetjeneste
og response. Det offentlige API-et er ikke endret.

## Edge cases og teststrategi

De eksisterende testene bevares. Tilleggstestene dokumenterer:

- inklusiv start og eksklusiv slutt
- tom periode
- omvendt periode
- helg ved begge periodegrenser
- fravær på helg og utenfor perioden

Testene er vanlige enhetstester av `CapacityPlanner`; ingen Spring-kontekst er
nødvendig.

## Designvalg, alternativer og trade-offs

En vanlig `while`-løkke kunne vært like korrekt og litt mer allment kjent.
Sekvensen er valgt fordi avgrensningen og filtreringen blir tydelige, uten å
bygge en mellomliste. For svært lange perioder kunne man vurdert aritmetisk
beregning av hele uker, men det ville gjort et intervju-case mindre lesbart.

Å returnere 0 ved `from > to` ble forkastet fordi det skjuler en ugyldig
forespørsel. En eksplisitt periodetype kunne eid denne invarianten i et større
domene, men er unødvendig for én liten operasjon.

## Kort intervjuforklaring

«Jeg valgte `[from, to)` fordi den gir komponerbare perioder og en entydig tom
periode. Jeg validerer omvendte grenser tidlig, modellerer arbeidsdag som et
navngitt begrep og lar tester ved grensene bevise off-by-one-semantikken.»
