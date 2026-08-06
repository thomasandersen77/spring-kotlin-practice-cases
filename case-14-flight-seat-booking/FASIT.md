# Fasit – flight seat booking

## Domene og kontrakt

`Flight` er aggregatroten og konsistensgrensen for setereservasjoner. `SeatNumber` og `PassengerId` er validerte value objects; `SeatReservation` er intern state og `SeatReserved` et domain event etter en reell tilstandsendring.

## Invariants og designvalg

- Sete- og passasjer-id kan ikke være blanke.
- Et sete kan ha høyst én reservasjon.
- En passasjer kan ha høyst ett sete på flyet.
- Bare en eksisterende reservasjon kan kanselleres.
- Etter kansellering kan setet og passasjeren bookes på nytt.
- Reservasjonene eksponeres som read-only snapshot.

I et større system kunne passasjerregelen vært konfigurert for grupper/spesialseter, og kansellering kunne sendt eget event. Her er exception ved ulovlig overgang et lite, tydelig valg.

## Edge cases og tester

Testene verifiserer faktisk state og event, dobbelsete, flere seter per passasjer, ukjent kansellering og rebooking.

## Kort intervjuforklaring

Dobbelbooking fanges i domenet før eventet opprettes. Databaseunikhet kan være et siste vern mot concurrency, men aggregatet uttrykker regelen og må lagres med en atomisk/optimistisk transaksjon.
