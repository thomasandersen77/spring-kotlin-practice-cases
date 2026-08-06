# Fasit – OAuth2/JWT resource server

Filterkjeden åpner `/public/**`, krever `SCOPE_reports:read` for rapportlisten, `ROLE_ADMIN` for delete og autentisering for øvrige kall. Resource server validerer JWT, bruker stateless sessions og har CSRF avslått for bearer-token-API-et.

Custom converter beholder standard scope-authorities og mapper `roles`-claim til `ROLE_*`. Dermed kan scopes uttrykke delegert API-tilgang og roller uttrykke applikasjonsrolle uten å miste noen av delene.

MockMvc-testene dekker 200/401/403/204, og en direkte convertertest beviser claim-mapping. I produksjon konfigureres issuer/JWKS og audience-validering; CSRF-vurderingen må endres dersom cookies brukes til autentisering.
