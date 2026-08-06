# Fasit – Case 01

## 1. Kort løsningsoversikt

Løsningen beholder den lille, rammeverksfrie domenemodellen og gjør reglene
eksplisitte i value objects og i den uttømmende rabattberegningen. Ugyldige
pengebeløp, antall og rabattprosenter stoppes ved konstruksjon. Prisingen
beregner subtotalen én gang, anvender valgt rabatt og sørger for at en fast
rabatt aldri gir negativ total.

## 2. Kontrakten løsningen oppfyller

- `Money` kan ikke representere et negativt beløp.
- `Quantity` må være større enn null.
- `Discount.Percentage` godtar verdier fra og med 0 til og med 100.
- `NoDiscount` returnerer kurvens subtotal.
- Prosentavslag reduserer subtotalen med den oppgitte prosenten.
- Fast rabatt trekkes fra subtotalen, men totalen gulvbegrenses til null.
- En tom kurv har subtotal og total lik null.
- Prosentberegninger avrundes til to desimaler med `RoundingMode.HALF_UP`.
- De eksisterende offentlige typene og funksjonssignaturene er bevart.

## 3. Domeneregler og invariants

`Money`, `Quantity` og `Discount.Percentage` validerer egne gyldighetsregler i
`init`. Dermed kan resten av modellen stole på at slike objekter er gyldige.
`Money.ZERO` er nøytralelementet når en kurv summeres. Regelen om at en rabatt
ikke kan skape en negativ total tilhører selve prisberegningen, fordi den
beskriver kombinasjonen av subtotal og rabatt – ikke rabattverdien isolert.

En fast rabatt på null er gyldig. En prosent på 0 eller 100 er også gyldig og
betyr henholdsvis ingen reduksjon og full reduksjon. Kurven tillater tom
linjeliste; subtotalen blir da null gjennom `fold`.

## 4. Viktigste Kotlin-idiomer

- Value classes for `CustomerId`, `ProductId` og `Quantity` reduserer primitive
  obsession uten runtime-overhead i vanlige tilfeller.
- `data class` gir verdibasert likhet for `Money`, `Basket` og linjene.
- `sealed class` sammen med en uttømmende `when` gjør alle rabattvarianter
  synlige for kompilatoren.
- Operatorene på `Money` uttrykker domeneregning uten å spre rå `BigDecimal`.
- `fold` summerer en read-only liste med `Money.ZERO` som startverdi.
- `require` uttrykker konstruktørinvariants med `IllegalArgumentException`.

## 5. Arkitektur- og designvalg

Caset trenger ikke Spring eller egne applikasjons-, adapter- og
persistenslag. `PricingService` er en liten domenetjeneste for regelen som
kombinerer en kurv og en rabatt. Verdier valideres av typen som eier regelen,
mens den kombinerte prisregelen ligger i tjenesten.

Rabatt er modellert som data i en `sealed class`. Det gir et lukket og tydelig
sett varianter, og `when` gjør manglende håndtering til en kompileringsfeil.
De eksisterende API-ene beholdes for å respektere oppgavens kontrakt.

## 6. Hvorfor løsningen passer tidsboksen

Løsningen introduserer ingen repositories, interfaces, factories eller ekstern
money-library. Den gjør bare de påkrevde reglene eksplisitte og dekker dem med
små JUnit-tester. Dette er et hensiktsmessig nivå for 45–60 minutter: modellen
er enkel å forklare, og utvidelsespunktene er synlige uten forhåndsbygget
arkitektur.

## 7. Alternative løsninger som ble vurdert

En strategi per rabatttype kunne flyttet beregningen inn i polymorfe objekter.
Det er mer aktuelt dersom rabattalgoritmene får egne avhengigheter eller mange
varianter. For tre små, lukkede varianter er en sealed modell og uttømmende
`when` mer direkte.

`Money` kunne alltid ha normalisert alle innkommende beløp til to desimaler.
Uten valuta eller eksplisitt krav om minste valutaenhet ville det forkastet
eller avrundet informasjon tidligere enn nødvendig. Derfor beholdes eksakte
innverdier og eksakt addisjon/multiplikasjon, mens resultatet av
prosentberegningen avrundes på den definerte pengegrensen.

En ekstern money-library kunne modellert valuta, minor units og avanserte
avrundingsregler. Det er utenfor kontrakten og tidsboksen.

## 8. Konkrete trade-offs

- `Money` har ingen valuta. Det holder caset lite, men forhindrer ikke at beløp
  fra ulike valutaer kombineres.
- `Money.minus` avviser indirekte et negativt resultat gjennom konstruktørens
  invariant. Prisreglen bruker derfor eksplisitt gulvbegrensning for fast
  rabatt.
- Read-only `List` begrenser mutasjon gjennom `Basket`, men er ikke en garanti
  for dyp immutabilitet dersom en muterbar liste beholdes et annet sted.
- Avrundingspolitikken er en enkel global case-konstant. Et system med flere
  valutaer måtte knyttet skala og avrunding til valuta eller priskontekst.

## 9. Teststrategi og edge cases

Testene dekker subtotal og alle tre rabattvarianter. De verifiserer negative
pengebeløp, null og negativt antall, rabattprosent rett innenfor og utenfor
grensene, tom kurv, 0 og 100 prosent, fast rabatt under, lik og over subtotal,
samt et halv-cent-resultat som dokumenterer `HALF_UP` til to desimaler.

Testene er vanlige JUnit 5-tester og starter ingen Spring-kontekst.

## 10. Mulige produksjonsutvidelser

Et produksjonssystem ville normalt modellert valuta, definert regler for
veksling og skala per valuta, brukt en etablert money-type og avklart hvordan
avgift, kampanjer og flere samtidige rabatter komponeres. ID-ene kunne validert
format og blanke verdier hvis innlesningskontrakten krevde det. Ved eksterne
avhengigheter eller dynamiske rabattregler kunne rabattstrategier og et
applikasjonslag vært berettiget.

## 11. Korte svar på debriefspørsmålene

1. **Hvorfor `Money`?** Den samler gyldighetsregel og aritmetikk i én
   verdibasert type, hindrer negative normaltilstander og unngår binær
   flyttallsfeil fra `Double`.
2. **Hvorfor `sealed class`?** Rabattvariantene er få og lukkede, og en
   uttømmende `when` gir synlig og kompilatorsjekket kontrollflyt. Strategier er
   mer passende når variantene er åpne, komplekse eller har avhengigheter.
3. **Hvorfor konstruktørvalidering?** Ugyldige verdiobjekter stoppes ved
   grensen, slik at alle konsumenter kan stole på invariantene. Validering bare
   i `PricingService` ville latt ugyldige objekter flyte gjennom domenet.
4. **Hvorfor sen avrunding?** Innverdier og mellomregning beholdes eksakte.
   Bare det ferdige prosentberegnede pengebeløpet avrundes. Tidlig avrunding av
   prosentfaktoren kan forplante et presisjonstap til hele subtotalen.
