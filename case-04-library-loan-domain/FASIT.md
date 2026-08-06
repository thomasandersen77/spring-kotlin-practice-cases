# Fasit – library loan domain

## Domene og kontrakt

`Loan` er en entity identifisert gjennom bok/låner-relasjonen og bærer status samt om engangsforlengelsen er brukt. `BookId`, `BorrowerId` og `LoanPeriod` er value objects. Perioden håndhever at forfall er etter utlånsdato.

## Invariants og designvalg

- Bare aktive lån kan forlenges.
- Forlengelsen kan brukes én gang og må være 1–30 dager.
- Forlengelse returnerer en ny `Loan`; originalen endres ikke.
- Et aktivt lån er forfalt først dagen etter forfallsdato. Returnerte lån rapporterer alltid 0 forfalte dager.

Immutable state gjør overgangen synlig og enkel å teste. I en ORM-modell kunne kontrollert mutasjon vært mer praktisk, men den samme invarianten må fortsatt ligge i entityen. Maksgrensen på 30 dager er en eksplisitt, lokal policy.

## Edge cases og tester

Testene dekker ugyldig periode, vellykket forlengelse, andre gangs forlengelse, returnert lån, ugyldig dagantall og dato før/på/etter forfall.

## Kort intervjuforklaring

Lånet er entityen fordi state og livsløp betyr noe; perioden er et value object fordi den defineres av verdiene sine. `extensionUsed` må være state, ellers kan engangsregelen ikke håndheves. Dato sendes inn, så logikken er deterministisk uten systemklokke.
