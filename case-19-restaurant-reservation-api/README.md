# Case 19 - Restaurant reservation API

## Domene
Restaurantbooking

## Tid
75-90 minutter

## Hva dette trener
- Thin controller
- Validation
- Use case
- API-/domain-grense

## Scenario
En reservasjon skal opprettes fra kundens navn, antall personer og ønsket tidspunkt. Modulen har foreløpig et use case og DTO-er, men mangler validering, domenespråk og eventuell controller-grense.

## Oppgave
Gjør reservasjonsflyten tydelig fra request til use case-resultat. Du skal kunne forklare hva som er API-validering, hva som er domeneregel, og hva som eventuelt ville krevd persistence i en ekte app.

## TODO / fokusområder
- TODO: Valider kundenavn, party size og reservasjonstidspunkt med tydelige feil.
- TODO: Avklar åpningstider, maks/min party size og om reservasjoner i fortiden skal avvises.
- TODO: Returner en meningsfull status, men vurder om status bør være enum i stedet for fri tekst.
- TODO: Legg til en tynn controller eller forklar hvorfor use case-test er nok for intervjuets nivå.
- TODO: Diskuter hvor kapasitets-/bordtilgjengelighet ville hørt hjemme dersom caset ble utvidet.

## Akseptansekriterier
- Gyldig request gir akseptert reservasjon med id.
- Minst to ugyldige requests er testet.
- Use case-laget eier flyten, ikke controlleren.
- DTO-er og domenebegreper er ikke unødvendig blandet sammen.

## Formål i intervjuet
Målet er ikke bare å få tester grønne, men å vise hvordan du oversetter krav til tydelige domenevalg, holder lagdelte grenser rene og forklarer trade-offs under tidspress.
I debrief bør du kunne begrunne hvilke regler som ble kodet i domenet, hva som ble liggende i application/API-lag, og hvilke forenklinger som var bevisste intervjuvalg.
## Ikke gjør det for lett
Ikke godta alle reservasjoner bare fordi testen gjør det. Legg inn nok validering til at use case-designet må diskuteres.

## Kommandoer

```bash
mvn -pl case-19-restaurant-reservation-api test
```
