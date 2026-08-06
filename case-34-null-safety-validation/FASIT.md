# Fasit – Case 34

## Oppgaver og kontrakt

Løsningen normaliserer nullable skjemadata, parser ISO-dato trygt, validerer
alle felt med maksimalt én feil per felt og bygger `Customer` bare når alt er
gyldig. Feil sorteres alfabetisk på feltnavn. Batchfunksjonene returnerer bare
gyldige kunder eller flater ut alle feil i skjemarekkefølge.

Påkrevde felter blir ikke nullable i domenet. Telefon og referansekode forblir
nullable fordi de faktisk er valgfrie. Samtykke normaliseres fra `null` til
`false`.

## Valgt løsning og Kotlin-konsepter

`normalizedOrNull` er en expression body med safe call og `takeIf`.
Datoparsing bruker `runCatching().getOrNull()`, slik at forventet brukerfeil
ikke blir exceptions.

Hver private feltvalidator returnerer `FieldValidation<T>` med normalisert
verdi eller én `FieldError`. Hovedvalidatoren kjører alle validatorene, samler
og sorterer feil med `listOfNotNull`, og konstruerer kunden først når listen er
tom. `requireNotNull` dokumenterer da en intern invariant, ikke validering av
brukerinput; ugyldig input har allerede blitt returnert som `Invalid`.

Batchene bruker `mapNotNull`, `filterIsInstance` og `flatMap`. Uttømmende
`when` over `ValidationResult` gjør begge resultatgrener synlige. Ingen `!!`,
`lateinit` eller mutabel offentlig tilstand brukes.

## Edge cases og teststrategi

Testene dekker null, blanke og ugyldige verdier, eksakte feilmeldinger,
e-postformat, intern whitespace, attenårsgrensen, postnummer, valgfri telefon,
referansekode, samtykke, flere samtidige feil og batchrekkefølge. Eksisterende
tester er omfattende nok til å dokumentere kontrakten.

## Designvalg, alternativer og trade-offs

En egen `ValidationResult` per felt ville gitt mer generell komposisjon, men
også mer mønstermatching og støy i et lite case. `Pair<T?, FieldError?>` er
kortere, men navnene `value` og `error` gjør invarianten lettere å forstå.

Fail-fast passer for programmeringsfeil og brudd på interne invariants. Et
selvbetjeningsskjema trenger feilakkumulering for at brukeren skal kunne rette
alt i én runde. I en større applikasjon kunne formatvalidering ligget i
applikasjonsgrensen, mens stabile domeneinvariants ble flyttet inn i value
objects.

`Invalid` kan implementere `ValidationResult<Nothing>` fordi `Nothing` er
subtype av alle typer, og resultattypen er kovariant (`out T`). Den samme
feilgrenen kan derfor brukes som `ValidationResult<Customer>` uten en falsk
customer-verdi.

## Kort intervjuforklaring

«Jeg gjør grensen fra upålitelig form-data til trygg domenedata eksplisitt.
Hvert felt valideres uavhengig og gir høyst én feil, alle feil akkumuleres, og
først en feilfri samling tillater konstruksjon av den ikke-nullbare
domenetypen.»
