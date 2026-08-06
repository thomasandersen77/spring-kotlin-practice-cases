# Fasit – warehouse pick list

## Domene og kontrakt

`PickList` er aggregatroten og eier linjer og status. `Sku` og `Quantity` er value objects; `PickLine` er intern linjetilstand.

## Invariants og designvalg

- SKU kan ikke være blank, og quantity må være positiv.
- Samme SKU avvises fremfor å merges; dette unngår å endre en allerede plukket linje implisitt.
- Bare åpne lister kan endres.
- Bare en kjent, uplukket linje kan markeres plukket.
- Listen må være ikke-tom og alle linjer må være plukket før fullføring.
- Intern mutasjon kapsles inn; lesere får snapshots av linjene og read-only status.

Resultatobjekter kunne modellert forventede brukerfeil uten exceptions. Her brukes `require` for ugyldige argumenter og `check` for ulovlig aggregatstate, som er kompakt og tydelig i et intervju.

## Edge cases og tester

Testene dekker gyldig linje, null quantity, duplikat, ukjent/gjentatt plukk, tom eller uferdig fullføring og endring etter fullføring.

## Kort intervjuforklaring

Aggregatet må eie både linjene og status for å beskytte overgangene. Ingen utenforstående får en muterbar samling. `complete` er den eneste overgangen til `COMPLETED` og kontrollerer hele invarianten atomisk.
