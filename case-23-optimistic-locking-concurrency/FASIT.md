# Fasit – optimistic locking concurrency

Aggregatet validerer SKU, ikke-negativ state/version og positiv reservasjon innen tilgjengelig beholdning. En gyldig reservasjon flytter quantity og øker versjonen.

Use caset laster state, skiller domenefeil til `Rejected`, og lagrer med den opprinnelige versjonen som `expectedVersion`. Repositoryets `false` blir `Conflict`; bare vellykket compare-and-set blir `Accepted`.

Testene dekker stateendring, expected version, accepted, stale konflikt, ugyldig quantity/lager og manglende aggregate. Optimistic locking passer ved korte, sjeldne konflikter; pessimistic locking kan være bedre ved svært høy konfliktgrad og dyre retries.
