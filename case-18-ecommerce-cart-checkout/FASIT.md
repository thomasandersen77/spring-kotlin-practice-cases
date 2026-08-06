# Fasit – ecommerce cart checkout

## Domene og kontrakt

`Cart` er aggregatroten og eier linjer, status og checkout-tidspunkt. `CheckoutCartUseCase` er application boundary og delegerer selve invarianten til aggregatet.

## Invariants og designvalg

- Produkt-id kan ikke være blank, og nye antall må være positive.
- Gjentatt produkt merges ved å summere quantity med eksplisitt overflow-feil.
- Bare en åpen cart kan få linjer eller sjekkes ut.
- Tom cart kan ikke sjekkes ut.
- Checkout endrer status og registrerer det innsendte, testbare tidspunktet; dobbel checkout avvises.
- Intern linjesamling eksponeres som snapshot.

I et Spring/JPA-system ville use case-metoden vært transaksjonsgrensen: hent cart fra repository, kall domenemetoden og lagre med optimistisk låsing. Et repository-interface er utelatt her fordi caset ikke har identitet eller persistence og en tom port ville vært seremoni.

## Edge cases og tester

Testene dekker kontrollert feil for tom cart, ugyldig quantity, merge, tilstand/tidspunkt, dobbel checkout og endring etter checkout.

## Kort intervjuforklaring

Use caset orkestrerer, mens `Cart` beskytter reglene. Checkout er en reell overgang, ikke en tom eller tilfeldig idempotent kommando. I produksjon må hele load–modify–save-sekvensen være én transaksjon.
