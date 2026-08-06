# Fasit – parking pricing rules

## Domene og kontrakt

`ParkingPriceCalculator` er en domain service fordi prisen kombinerer en parkeringsøkt, tariff og kjøretøyregel uten å høre naturlig hjemme hos ett av value objectene.

## Regler og designvalg

- Slutt må være etter start; under 15 minutter er gratis, mens nøyaktig 15 minutter prises.
- All betalbar tid avrundes opp til påbegynt time.
- Bil betaler 100 %, motorsykkel 50 % og elbil 75 % av ordinær pris.
- En økt som starter mellom 22:00 og 05:59 får `nightMax` som tak.
- Resultatet avrundes til to desimaler med `HALF_UP`.

Kjøretøyfaktorene er navngitt gjennom enum-regelen, ikke spredte magiske tall. Et rikere tariffsett med tidssegmenter ville vært nødvendig for økter som skal splittes mellom dag og natt; denne løsningen velger starttid som en enkel, eksplisitt kontrakt.

## Edge cases og tester

Testene dekker gratisgrensen, nøyaktig 15 minutter, påbegynt time, alle kjøretøytyper, nattmaks og ugyldig intervall. `Money` avviser negative beløp.

## Kort intervjuforklaring

Jeg valgte påbegynt time fordi regelen er lett å etterregne. Kritiske terskler er konstanter, og BigDecimal avrundes bare ved sluttresultatet. Ved mer avanserte tariffperioder ville jeg introdusert eksplisitte prisintervaller fremfor flere betingelser.
