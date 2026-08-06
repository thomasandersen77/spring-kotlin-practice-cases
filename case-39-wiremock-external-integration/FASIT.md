# Fasit – Spring Boot-integrasjon med WireMock

ACL-et oversetter `FREE/PARTIAL/BUSY` til internt domenespråk, validerer id, datoer, rekkefølge og prosent, og normaliserer skills. Service avhenger bare av `CapacityPort` og mapper til stabil norsk response-status.

RestClient-adapteren sender korrekt path/API-key og skiller 404, 5xx/timeout og ugyldig JSON/status. Connect/read timeout er 500 ms, slik en treg leverandør blir `ProviderUnavailable`.

Testene bruker ren mapper/fake service og WireMock for 200, header/path, 404, 500, JSON-feil, ukjent kode og timeout. Retries bør bare brukes for transiente/idempotente kall med begrensning og observability.
