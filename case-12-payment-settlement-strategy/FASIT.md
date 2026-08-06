# Fasit – payment settlement strategy

Calculatoren indekserer strategier per betalingsmetode og kjenner ikke gebyrformlene. Standardene er kort 1,5 %, faktura 25, mobil 0 og internasjonalt kort 2,5 % med minimum 30. Resultater avrundes til to desimaler med `HALF_UP`; negative beløp avvises.

Tom strategiliste gir trygge standarder av hensyn til starttesten. En eksplisitt, ikke-tom liste er full konfigurasjon, og manglende metode feiler tydelig. Ny metode krever ny strategi, ikke endring i calculatorens flyt.

Testene verifiserer alle regeltyper, minimum, faktisk strategioppslag og manglende strategi. En sealed `when` er enklere for et lukket regelsett; Strategy er valgt fordi betalingsmetoder skal kunne utvides og injiseres.
