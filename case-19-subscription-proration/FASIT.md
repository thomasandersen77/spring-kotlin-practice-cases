# Fasit – subscription proration

## Domene og kontrakt

`ProrationCalculator` beregner bare ekstra belastning ved oppgradering i én inkluderende fakturaperiode. `changeDate` er første dag kunden bruker og betaler for ny plan.

## Invariants og beregning

- Planpris kan ikke være negativ; periodens slutt kan ikke være før start.
- Endringsdato må ligge i perioden.
- Både periodens start/slutt og endringsdato/slutt telles inkluderende.
- Prisforskjellen fordeles etter `remainingDays / totalDays`.
- Lik pris og nedgradering gir 0 i upgrade charge; eventuell kreditering er en separat use case.
- Resultatet avrundes til to desimaler med `HALF_UP` etter beregningen.

Alternativet ved nedgradering er negativ kreditnota. Å returnere null holder metoden tro mot navnet `calculateUpgradeCharge`, men policyvalget må være dokumentert. En daglig avrundet sats ville kunne akkumulere en annen sum; her prorateres totaldifferansen direkte.

## Edge cases og tester

Testene etterregner en midtmånedsendring med ikke-heltallig dagsandel, første og siste dag, lik pris, nedgradering, ugyldig periode og dato utenfor perioden.

## Kort intervjuforklaring

Jeg gjør inkluderende semantikk eksplisitt med `+1` både i teller og nevner. Endring på første dag gir full differanse, siste dag gir én dags andel. BigDecimal brukes hele veien og avrundes én gang til slutt.
