# Fasit – bank transfer fullstack JPA

`Money` lagrer hele øre og konverterer eksakt til/fra BigDecimal med maksimalt to desimaler. `BankAccount` beskytter positiv credit/debit, blokkert status og dekning. `BankingService.transfer` er den offentlige transaksjonsgrensen som laster begge aggregates, debiterer/krediterer, lagrer begge og registrerer én transfer med injisert klokke.

Repository-portene skjuler Spring Data. Adapterne bevarer JPA-versjon, og eksplisitte mappere holder entity/DTO ute av domenet. Controlleren delegerer; exception handler gir 400/404/409/422.

Testpakken dekker domene, money, mappings, JPA-versjon, REST og reell rollback når transfer-save feiler. `@Transactional` må være på proxied public service-metode; private/self-invoked metoder får ikke ny Spring-proxygrense.
