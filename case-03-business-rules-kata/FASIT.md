# Fasit – business rules kata

## Domene og kontrakt

`InvoiceScoring` validerer fakturalinjer og beregner `subtotal`, samlet rabatt og `total`. Fakturaen må ha minst én linje, pris kan ikke være negativ, og antall må være positivt. Ukjente rabattkoder avvises eksplisitt.

## Regler og designvalg

- VIP gir 10 % av subtotalen med heltallsavrunding nedover.
- `SAVE50` gir 50, `SAVE10` gir 10 %, og `HALF` gir 50 %.
- VIP og kode stables, begge beregnes fra subtotalen, og samlet rabatt begrenses til subtotalen.
- Kodefunksjoner i et lite map gjør nye regler enkle å legge til uten et stort `when` eller et generisk regelverk.

En egen `Money`-type eller desimalbeløp ville vært riktig ved valuta med øre. Her beholdes den offentlige `Int`-kontrakten. Et strategi-interface gir mer metadata og avhengigheter, men er unødvendig for tre rene regler.

## Edge cases og tester

Testene dekker ingen rabatt, stabling, prosentkode, rabattgulv, tom faktura, ugyldige linjer og ukjent kode. `multiplyExact`/`addExact` gjør overflow eksplisitt.

## Kort intervjuforklaring

Jeg holder validering og rabattregler i domenetjenesten. Stablingsrekkefølgen er bevisst enkel: alle rabatter beregnes fra samme subtotal, summeres og begrenses. Dersom rabattene senere får vilkår, prioritet eller eksterne data, ville jeg flyttet dem til navngitte strategiobjekter.
