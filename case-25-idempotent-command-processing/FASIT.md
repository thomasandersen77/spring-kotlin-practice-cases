# Fasit – idempotent command processing

Nøkkelen normaliseres ved konstruksjon. Use caset validerer kunde/beløp, returnerer lagret receipt uten gateway-kall, eller charger og lagrer resultatet. `Processed` og `AlreadyProcessed` skiller nytt kall fra retry. Lagringskonflikt leser vinnerresultatet; manglende vinner blir eksplisitt teknisk feil.

`@Synchronized` beskytter samtidige kall i samme instans. I distribuert produksjon må store tilby atomisk claim/reservation før gateway-kallet; skeleton-portens `save` etter charge kan ellers ikke alene forhindre cross-node-race. Gatewayens egen idempotency key bør også brukes.

Testene beviser normalisering, én charge ved duplikat og at domenefeil stopper før gateway.
