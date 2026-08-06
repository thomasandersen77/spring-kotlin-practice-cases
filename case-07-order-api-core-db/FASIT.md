# Fasit – order API via core til database

`Order` er aggregatroten og beskytter at ordren har minst én linje. `OrderLine` krever positiv quantity og pris. Totalen beregnes fra linjene. Gyldige overganger er `PENDING -> CONFIRMED` og `PENDING/CONFIRMED -> CANCELLED`; en kansellert ordre kan ikke endres videre.

Domain-modellen er fri for Spring, HTTP og JPA. `OrderService` er transaksjonsgrensen og avhenger av `OrderRepository`-porten. `JpaOrderRepository` mapper til/fra entities innenfor transaksjon, og controlleren mapper bare DTO-er.

Testene er rene enhetstester og dekker total, tom ordre, ugyldig linje og positive/negative statusoverganger. I produksjon ville optimistic locking vært nødvendig mot samtidige bekreftelser/kanselleringer.

Kort intervjuforklaring: linjer lever innenfor ordren fordi validering og status må være konsistent samlet; Spring Data er en adapter, ikke en avhengighet i core.
