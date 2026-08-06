# Fasit – Case 32

## Oppgaver og kontrakt

Løsningen implementerer kanalnavn, kanalvalg, prioritet, avvisningsårsak,
resultatbeskrivelse, leveringsrapport og stabil prioritetssortering. Kanalrekkene
følger kontrakten nøyaktig, markedsføring krever samtykke og e-post, og
avvisningsårsaken gjenbruker kanalvalget.

## Valgt løsning og Kotlin-konsepter

Alle regler over `Channel`, `Notification` og `DeliveryResult` er uttømmende
`when`-uttrykk uten `else`. Kompilatoren vil dermed peke på alle relevante
regler dersom hierarkiet utvides. Smart casts gir tilgang til variantdata uten
manuelle casts eller `!!`.

`listOfNotNull` og `let` bygger kanalrekkene uten mutasjon. Rapporten bruker
`count`, `filterIsInstance` og `minOfOrNull`. `sortedBy` er stabil og bruker
prioritetens deklarerte enum-rekkefølge: `HIGH`, `NORMAL`, `LOW`.

## Edge cases og teststrategi

De eksisterende testene dekker mottakere med komplett, delvis og manglende
kontaktinformasjon, manglende samtykke, alle prioriteter og resultatvarianter,
tom rapport, korteste retry-frist og stabil sortering innen samme prioritet.
Ingen ekstra tester er nødvendige for den oppgitte kontrakten.

## Designvalg, alternativer og trade-offs

Reglene ligger i en stateless `NotificationPolicy`, ikke som polymorfe metoder
på variantene. Dette holder varseldataene enkle og samler en policy som kan
endre seg uavhengig av meldingens struktur. Dersom hver variant eide stabil og
lokal oppførsel, kunne polymorfi redusert antallet eksterne `when`-uttrykk.

Et enum alene ville ikke modellert variantspecifikke data som ordre-ID,
reset-token eller kampanjeinformasjon. En åpen klasse ville mistet
kompilatorens uttømmende kontroll. `sealed interface` tillater et lukket
variantsett uten å bruke opp en klassearv.

Prioritetssorteringen bruker enum-ordinal fordi rekkefølgen er en eksplisitt
del av enum-definisjonen her. Dersom enum-rekkefølgen ikke var domenekontrakt,
burde en eksplisitt numerisk rang brukes.

## Kort intervjuforklaring

«Sealed-hierarkiene modellerer et lukket sett varianter med ulik data. Jeg lar
alle `when` være uttømmende, så en ny variant gir compile-feil i hver regel som
må vurderes. Kanalvalget er eneste sannhetskilde og gjenbrukes når en
avvisningsårsak skal bestemmes.»
