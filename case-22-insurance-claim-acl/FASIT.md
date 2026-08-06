# Fasit – insurance claim ACL

`ClaimTranslator` oversetter nullable strenger og leverandørkoder til `InsuranceClaim`, `ClaimType`, `BigDecimal`, `Currency` og `LocalDate`. `HOME` oversettes eksplisitt til domenets `HOUSE`; ukjente koder gjettes ikke.

Kontrollerte failures brukes for manglende/ukjent type, kunde, datoformat, fremtidig dato, ugyldig/ikke-positivt beløp og ugyldig ISO-valuta. Klokken injiseres for deterministisk datovalidering.

Testene verifiserer hele success-objektet og seks ulike failure-flyter. Resultattype passer fordi leverandørfeil er forventede mappingutfall; exceptions reserveres for uventede tekniske feil.
