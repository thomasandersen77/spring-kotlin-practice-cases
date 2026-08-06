# Fasit – hospital triage policy

## Domene og kontrakt

`TriagePolicy` er en deterministisk domain service: samme validerte request gir alltid samme prioritet. Den er et treningsregelsett, ikke medisinsk beslutningsstøtte.

## Regler og prioritet

1. `CRITICAL` symptom eller oksygen under 90 gir `IMMEDIATE`.
2. Oksygen under 95, feber minst 39,5, smerte minst 7 eller ventetid minst 120 minutter gir `URGENT`.
3. Alder under 1 eller minst 75 eskalerer bare sammen med feber minst 38 eller smerte minst 5.
4. Ellers blir prioriteten `STANDARD`.

Guard clauses validerer alder 0–130, smerte 0–10, ikke-negativ ventetid, oksygen 0–100 og temperatur 30–45. Kritiske regler evalueres først, så en mildere regel aldri kan nedprioritere dem.

## Alternativer, edge cases og tester

Reglene kunne vært separate strategiobjekter med medisinsk konfigurasjon. For dette lille caset er navngitte predikater og prioritert flyt mer lesbart. Testene dekker alle prioriteter, terskler, regelkonflikt, alder alene og ugyldige verdier.

## Kort intervjuforklaring

Jeg validerer først, returnerer deretter på høyeste alvorlighet og gjør alder til en modifikator, ikke selvstendig diagnose. Nye kliniske regler bør ha eksplisitt prioritet og egne tester før policyen eventuelt blir datadrevet.
