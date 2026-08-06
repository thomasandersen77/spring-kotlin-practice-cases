# Fasit – refaktorer en feit controller

Controlleren bruker typed request/response og mapper til `CreateSubscriptionCommand`. `SubscriptionService` orkestrerer opprettelse og kansellering mot `SubscriptionRepository`; JPA er skjult bak adapteren. `Subscription`, `Plan` og den idempotente `cancel`-overgangen er domenet.

Prisene 99/199/499 ligger hos enum-verdiene og kan testes uten Spring. Ukjent plan og ugyldig UUID gir bevisste `IllegalArgumentException`. Legacy-map-metodene beholdes for eksisterende kontrakt, men den nye HTTP-grensen bruker ikke maps.

Testene dekker alle priser, ukjent plan, ugyldig customerId og idempotent kansellering. I produksjon ville klokke/id-generator injiseres og exceptions mappes til strukturerte HTTP-feil.

Kort intervjuforklaring: controlleren mapper, application service orkestrerer, domenet eier pris og state, og adapteren eier JPA-mapping.
