# Case 10 - Shipping slot aggregate

## Domene
Logistikk / levering

## Tid
45-60 minutter

## Hva dette trener
- Aggregate Root
- Value Objects
- Invariants
- Sortering og konsistens rundt bookinger

## Scenario
En leveringsrute har tidsvinduer og kapasitet per slot. Koden har `TimeWindow`, `DeliverySlot` og `PackageBooking`, men selve bookingreglene mangler.

## Oppgave
Implementer bookinglogikk som beskytter invariants rundt kapasitet, duplikate pakker og tidsrekkefølge. Vurder om `DeliveryRoute` skal eie state, eller om metodene skal forbli mer funksjonelle/stateless.

## TODO / fokusområder
- TODO: Hindre booking når slot-kapasiteten er brukt opp.
- TODO: Avklar om samme `packageId` kan bookes flere ganger i samme eller ulike slots.
- TODO: Implementer sortering på en måte som faktisk kjenner slotenes starttid, ikke bare bookingtidspunkt.
- TODO: Legg til tester for full slot, duplikat og ugyldig tidsvindu.
- TODO: Forklar hvor aggregatgrensen går: route, slot eller booking?

## Akseptansekriterier
- Domenereglene ligger nær objektene de gjelder.
- Testene dekker både vellykket booking og minst to negative flyter.
- Koden er enkel å forklare høyt uten å introdusere unødvendig infrastruktur.

## Formål i treningen
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste treningsvalg.

## Ikke gjør det for lett
Ikke bare returner en booking fra `book`. Modellen må vise hvordan kapasitet og duplikater håndheves.

## Treningsspørsmål / debrief
1. Hvor går aggregatgrensen: route, slot eller booking? Begrunn.
2. Hvordan håndhever du kapasitet uten database?
3. Hvorfor er sortering på bookingtidspunkt feil i dette domenet?

## Kommandoer

```bash
./mvnw test -pl case-10-shipping-slot-aggregate
```
