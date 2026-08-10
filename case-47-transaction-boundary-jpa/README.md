# Case 47 – Transaction boundary med JPA og H2

## Domene
Påmelding til et arrangement med begrenset kapasitet.

## Tid
60–90 minutter.

## Vanskelighetsgrad
Medium.

## Hva dette trener
- application service som transaksjonsgrense
- `@Transactional`
- Spring Data JPA og H2
- atomisk oppdatering av kapasitet og registrering
- rollback ved exception
- databaseconstraint som siste forsvarslinje
- enkel domene-/persistensmodell uten bank-overføring

## Scenario
Når en deltaker melder seg på, skal én ledig plass reserveres og en registrering lagres. Hvis registreringen feiler, må kapasiteten rulles tilbake. Samme e-post kan bare registreres én gang per arrangement.

## Oppgave
Implementer kapasitetsinvarianten og `RegistrationService.register` som én atomisk operasjon. Test både vellykket lagring og rollback.

## Gjeldende kontrakt
- Ukjent arrangement gir `EventNotFound`.
- Ingen ledige plasser gir `NoSeatsAvailable`.
- Duplikat e-post for samme arrangement gir `AlreadyRegistered`.
- Vellykket registrering reduserer kapasiteten med én.
- Kapasitet og registrering committes eller rulles tilbake samlet.
- Samme e-post kan brukes på ulike arrangementer.

## TODO / fokusområder
1. Beskytt `availableSeats`-invarianten i entity eller domeneobjekt.
2. Plasser `@Transactional` på application service.
3. Hent arrangement og håndter ukjent id.
4. Håndter duplikat både applikasjonsvennlig og med databaseconstraint.
5. Lagre og flush når det er nødvendig for forutsigbar feilsemantikk.
6. Bevis rollback med integrasjonstest.
7. Diskuter samtidige siste-plass-reservasjoner uten å overimplementere locking.

## Forslag til arbeidsrekkefølge
1. Få invarianttesten for kapasitet grønn.
2. Implementer happy path i én transaksjon.
3. Legg til duplikat- og tom-kapasitetstest.
4. Kontroller faktisk databasezustand etter exception.

## Akseptansekriterier
- Happy path lagrer begge endringer.
- Feil gir ingen delvis databaseeffekt.
- Transaksjonsgrensen ligger i service, ikke controller/repository.
- Unik constraint beskytter datalaget.
- `open-in-view` er deaktivert.
- H2 brukes bevisst som treningsdatabase.

## Ikke gjør det for lett
Ikke gjør dette til en ny bank-transfer, legg transaksjonen i testen eller repositoryet, fjern unik constraint eller nøye deg med å verifisere at en exception kastes uten å sjekke rollback.

## Formål i intervjuet
Caset gir et lite og forklarbart eksempel på hvorfor transaksjonsgrensen tilhører use caset og hvordan man beviser atomisitet.

## Intervjuspørsmål / debrief
1. Hvorfor ligger `@Transactional` på service-metoden?
2. Hva skjer med JPA dirty checking i denne flyten?
3. Hvordan beviser testen rollback og ikke bare exception?
4. Hvorfor trenger du både applikasjonssjekk og unik constraint?
5. Hvilke forskjeller kan H2 skjule mot PostgreSQL?
6. Hvordan ville du håndtert to samtidige reservasjoner av siste plass?
7. Når er eksplisitt mapping mellom domene og entity verdt kostnaden?

## Kommandoer
```bash
./mvnw test -pl case-47-transaction-boundary-jpa
./mvnw verify -pl case-47-transaction-boundary-jpa
```
