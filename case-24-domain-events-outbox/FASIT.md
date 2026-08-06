# Fasit – domain events outbox

Use caset validerer kommandoen og lager `PurchaseOrder`, det transportuavhengige eventet `OrderPlaced` og deretter teknisk `OutboxMessage`. Ordre-save og outbox-append kjøres inne i én injisert `TransactionBoundary`.

I en databaseadapter skal grensen være en reell lokal transaksjon, slik at outbox-feil ruller ordre-write tilbake. Default-grensen er bare en enkel kjører for det infrastrukturløse caset; den lover ikke rollback alene.

Testene dekker eventmapping, én transaksjonsgrense, samsvar mellom ordre/melding, propagert outbox-feil og validering før writes. En separat relay publiserer senere og markerer meldingen levert med retry/idempotency.
