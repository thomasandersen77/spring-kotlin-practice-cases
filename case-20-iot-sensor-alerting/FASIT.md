# Fasit – IoT sensor alerting

Use caset velger en `SensorAlertRule` per sensortype og publiserer bare når regelen returnerer et brudd. Standardgrenser er temperatur over 80 °C, luftfuktighet over 90 % og vibrasjon over 10 mm/s. Sensor-id må finnes, verdien må være endelig, og fukt/vibrasjon kan ikke være negativ.

`AlertPublisher` er porten; testene bruker en enkel fake. En produksjonsadapter kunne publisert til Kafka, MQTT eller en hendelsestjeneste. Nye terskler kan injiseres uten endring i use case-flyten.

Testene dekker alert, ingen alert, alle sensortyper og ugyldig id/NaN/negativ verdi. En regeltabell er kortere for rene maksimumsgrenser; strategiobjekter gir rom for senere intervaller og hysterese.
