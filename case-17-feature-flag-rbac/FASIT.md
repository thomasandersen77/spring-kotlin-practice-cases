# Fasit – feature flag RBAC

Policyen validerer identitet og evaluerer produksjonsvern før roller. Eksperimentelle eller ikke-godkjente flagg kan aldri aktiveres i produksjon. Admin kan ellers aktivere i alle miljøer. Product owner må matche produktområde og kan bruke alle miljøer med produksjonsgodkjenning. Developer må matche området og er begrenset til DEV.

Manglende produktområde gir ingen tilgang for ikke-admin. Regelrekkefølgen hindrer at en bred admin-regel omgår produksjonsvernet.

Testene dekker developer, product owner, admin, produktområde, miljø, approval og experimental. Ved vesentlig større matrise ville tabellbaserte regler eller navngitte policies vært bedre enn flere boolske uttrykk.
