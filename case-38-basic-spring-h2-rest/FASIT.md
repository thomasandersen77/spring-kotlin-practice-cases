# Fasit – basic Spring Boot REST med H2

`ProductEntity.changeStock` beskytter ikke-negativ sluttbeholdning og overflow. Request/entity/response har eksplisitte mappere, og service-metodene eier transaksjonene for create/get/list/change. Aktiv-queryen sorterer i repositoryet.

Controlleren er tynn og returnerer aldri entity. Feil mapper not found til 404, stock conflict til 409 og ugyldig argument til 400; Bean Validation håndteres av Spring.

Testene dekker ren invariant, repository-query, servicemapping og HTTP 201/400/404. I produksjon bør unik SKU-konflikt få stabil 409-feil, og concurrency håndteres med optimistic locking.
