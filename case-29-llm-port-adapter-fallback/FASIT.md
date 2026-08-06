# Fasit – LLM port/adapter med fallback

Service validerer input, prøver primærmodellen og faller bare tilbake ved `LlmOverloadedException`. Parse-/valideringsfeil propagerer, fordi ny modell ikke reparerer en feil kontrakt. Adapterresultatet bærer modellen som faktisk svarte.

`ModelId` kan ikke være blank og `Score` håndhever 0–100 ved konstruksjon. Testene dekker primær, fallback, ingen fallback ved invalid response og hallusinert score.

Porten uttrykker use casets behov uten HTTP/JSON. Provideradapteren eier timeouts, circuit breaker og protokoll; prompt/structured-output-kontrakten bør ligge nær application policy. Én primær-retry kan øke kvalitet, men koster latency og belastning og er derfor ikke valgt her.
