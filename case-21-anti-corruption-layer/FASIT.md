# Fasit – anti-corruption layer

`CreditRiskTranslator` oversetter leverandørens score/red flag til `CreditRisk` og validerer scoreområdet 0–1000. `source_system` og eksterne feltnavn stopper i ACL-et. `CreditPolicy` ser bare `LoanApplication`.

Lav risiko godkjennes. Medium til og med 500 000 godkjennes, større beløp går til manuell behandling. Høy risiko over 100 000 avslås; lavere beløp går til manuell behandling. `CreditDecisionStatus` gjør manuell behandling eksplisitt uten å forveksle den med teknisk feil.

Application service injiserer provider, translator og policy. Testene dekker mappinggrenser, red flag, skjev score og alle beslutningstyper. Høy risiko er et gyldig domeneutfall, ikke en exception.
