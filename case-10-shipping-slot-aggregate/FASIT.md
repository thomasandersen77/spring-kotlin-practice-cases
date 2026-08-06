# Fasit – shipping slot aggregate

## Domene og kontrakt

`DeliveryRoute` er aggregatroten og eier kjente bookinger og slot-vinduer. `TimeWindow` er et value object, `DeliverySlot` beskriver kapasitet, og `PackageBooking` er resultatet av en godkjent reservasjon.

## Invariants og designvalg

- Tidsvindu må ha start før slutt; slot-id og pakke-id kan ikke være blanke; kapasitet må være positiv.
- En pakke kan bare bookes én gang på hele ruten, også på tvers av slots.
- Antall bookinger per slot kan ikke overstige kapasiteten.
- Samme slot-id kan ikke registreres med ulike tidsvinduer.
- Sortering bruker slotens starttid og deretter slot-/pakke-id som deterministiske tie-breakers.

Den eksisterende metoden mottar `existing`; disse importeres til aggregatets kjente state før invariants sjekkes. Et nytt system ville helst konstruert/hydrert aggregatet komplett og brukt `book(slot, packageId)` uten snapshot-parameter. `Clock` injiseres for testbar bookingtid.

## Edge cases og tester

Testene dekker gyldig booking, full kapasitet, duplikat på tvers av slots, sortering, positiv kapasitet og ugyldig tidsvindu.

## Kort intervjuforklaring

Ruten er konsistensgrensen fordi både global pakkeduplikasjon og slot-kapasitet må vurderes samlet. Sortering kan ikke bruke `bookedAt`; den forretningsmessige rekkefølgen er leveringsvinduets start.
