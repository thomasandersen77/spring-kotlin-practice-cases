# Fasit – bank account CRUD + JPA

`Money` normaliserer NOK til skala 2 med `HALF_EVEN`. `BankAccount` eier saldo: positive innskudd/uttak, ingen operasjoner på lukket konto, eksplisitt utilstrekkelig dekning og lukking bare ved nullsaldo. Saldo finnes ikke i update-DTO-en.

Application services eier transaksjoner, unikhetskontroller og slettingsregler. Kundeoppdatering kontrollerer e-post mot andre kunder. Konto-listing bruker faktisk customer-query, og deposit-endepunktet kaller domeneflyten. JPA-adapterne skjuler entities og lazy owner-relasjon.

Eksisterende domene-, service-, repository- og MockMvc-tester dekker avrunding, invariants, uniqueness, filtrering, CRUD og HTTP-feil. Databaseconstraints er siste vern; domenet gir tidligere og mer forklarlige feil. Optimistic locking/audit og soft close ville vært naturlige produksjonsutvidelser.
