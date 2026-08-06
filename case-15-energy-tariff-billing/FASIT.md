# Fasit – energy tariff billing

## Domene og kontrakt

Regningen beregner forbruk fra to målerstander, variabel energikostnad, fastledd, mva og total. `EnergyBill` beholder delresultatene slik at beløpet kan etterregnes.

## Invariants og beregning

- Målerstander og tariffkomponenter kan ikke være negative, og sluttmåling kan ikke være lavere enn start.
- Mva må være mellom 0 og 1.
- Forbruk er `slutt - start`.
- Variabel kostnad er forbruk ganger summen av spotpris og nettleie.
- Mva gjelder både variabel kostnad og fastledd.
- Mellomregninger beholdes eksakte; total avrundes én gang til to desimaler med `HALF_UP`.

Å avrunde hver komponent kan gi en annen total. Denne løsningen prioriterer matematisk sum og sluttavrunding. I faktiske avregningsregler måtte lov-/leverandørkrav avgjort både mva-grunnlag og avrundingspunkt.

## Edge cases og tester

Testene har en komplett etterregnbar faktura, ikke-heltallig øreverdi, baklengs måler og ugyldig mva. BigDecimal opprettes fra strenger i testene for å unngå binær flyttallsfeil.

## Kort intervjuforklaring

Jeg bruker value objects til å validere måling og tariff ved opprettelse, viser hvert beregningstrinn i resultatet og avrunder bare sluttbeløpet. `scale` er antall desimaler; `precision` er totalt antall signifikante siffer.
