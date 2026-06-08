# Case 01 - Order API via core-lag til database

Bygg et lite ordre-API med tynn controller, service/core-lag, domeneobjekter og JPA-persistens.

## Hvordan kjøre

```bash
mvn test
mvn spring-boot:run
```

## Intervjuøvelse

Tenk høyt mens du jobber:

- Hva er domenet?
- Hva er aggregate root?
- Hvor går transaksjonsgrensen?
- Hva skal ligge i controller, service, domain og repository?
- Hvor bør mapping skje?
- Hvordan ville du testet dette?
- Hva ville du forenklet i et ekte case med tidsbegrensning?

## Viktig

Dette prosjektet er med vilje ikke ferdig. Det inneholder TODO-er som du skal fullføre manuelt.
