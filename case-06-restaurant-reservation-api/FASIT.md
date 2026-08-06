# Fasit – restaurant reservation API

`CreateReservationUseCase` validerer og oppretter en reservasjon. Kundenavn må være utfylt, selskapet må bestå av 1–12 personer, tidspunktet må ligge i fremtiden og mellom 17:00 og 21:30. Gyldig request får generert id og status `ACCEPTED`.

Klokke og id-generator injiseres, slik at tids- og id-avhengig oppførsel er deterministisk i test. Fri tekst beholdes i response av hensyn til eksisterende API-kontrakt, men verdien produseres fra `ReservationStatus`.

Testene dekker happy path, blankt navn, for stort selskap, fortid og åpningstid. Bordkapasitet krever lagret restaurant-/bordstate og ville vært en separat domain service eller et aggregate lastet gjennom en repository-port. En eventuell controller skal bare mappe HTTP-data og delegere.

Kort intervjuforklaring: formatkrav kan ligge ved API-grensen, mens party size, fremtid og åpningstid er domeneregler og må derfor håndheves i use caset også.
