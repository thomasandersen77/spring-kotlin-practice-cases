# Fasit – Case 27

## Oppgaver og kontrakt

Profil og CV hentes parallelt. Profilen er obligatorisk: feil fra
`ProfileClient` propagerer og kansellerer søskenjobben. CV-en er valgfri for
sammendraget: hvis den ikke svarer innen den konfigurerte fristen, returneres
profilen med tom ferdighetsliste. Standard timeout er 1500 ms.

## Valgt løsning og coroutine-konsepter

`fetchSummary` bruker `coroutineScope`, og begge kall startes med `async` før
noen av dem ventes på. `await` henter resultatene. Scope-et eier begge jobbene,
venter på dem og kansellerer den andre dersom én jobb feiler. Det er structured
concurrency: levetiden til barnejobbene kan ikke lekke ut av operasjonen.

`withTimeoutOrNull` omslutter bare CV-kallet. Timeout oversettes dermed til
`null`, og `cv?.skills.orEmpty()` uttrykker den avtalte degraderingen. En
positiv timeout valideres ved konstruksjon.

## Edge cases og teststrategi

Testene bruker `runTest` og virtuell tid. De dokumenterer:

- korrekt sammenslåing av profil og CV
- parallelle kall med total tid lik det tregeste kallet
- CV-timeout med tom ferdighetsliste og eksakt virtuell frist
- profilfeil som kansellerer CV før den fullfører

Fake-klientene bruker `delay`, ikke `Thread.sleep`, slik at de er
kansellerbare og testene kjører på millisekunder.

## Designvalg, alternativer og trade-offs

En streng kontrakt kunne latt CV-timeout feile hele sammendraget med
`withTimeout`. Degradering er valgt fordi en profil fortsatt har verdi uten CV,
mens manglende profil gjør sammendraget meningsløst. `supervisorScope` er ikke
brukt: profilfeil skal nettopp kansellere CV-kallet.

Produksjonsadapteren som utfører blokkerende I/O bør selv bruke en passende
klient eller `withContext(Dispatchers.IO)`. Domenetjenesten bør ikke gjette om
et deklarert `suspend`-API blokkerer, og legger derfor ikke inn dispatcherbytte.

## Kort intervjuforklaring

«Jeg starter begge uavhengige kall med `async` i samme `coroutineScope`.
Profilfeil avbryter hele scopet og kansellerer CV-en. CV-timeout er derimot en
eksplisitt, lokal degraderingsregel. Virtuell tid beviser både parallellitet og
kansellering uten reell venting.»
