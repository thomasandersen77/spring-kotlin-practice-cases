package com.training.case47.registrations

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RegistrationServiceTest(
 @Autowired private val service: RegistrationService,
 @Autowired private val events: EventRepository,
 @Autowired private val registrations: RegistrationRepository
) {
 @BeforeEach
 fun clean() {
 registrations.deleteAll()
 events.deleteAll()
 }

 @Test
 fun `paamelding reduserer kapasitet og lagrer registrering atomisk`() {
 val event = events.save(EventEntity(name = "Kotlin-dag", availableSeats = 2))

 val receipt = service.register(event.id!!, "dev@example.no")

 assertThat(receipt.remainingSeats).isEqualTo(1)
 assertThat(events.findById(event.id!!).orElseThrow().availableSeats).isEqualTo(1)
 assertThat(registrations.count()).isEqualTo(1)
 }

 @Test
 fun `feil etterlater ikke delvis kapasitetsendring`() {
 val event = events.save(EventEntity(name = "Kotlin-dag", availableSeats = 1))
 service.register(event.id!!, "same@example.no")

 assertThatThrownBy { service.register(event.id!!, "same@example.no") }
 .isInstanceOf(AlreadyRegistered::class.java)

 assertThat(events.findById(event.id!!).orElseThrow().availableSeats).isZero()
 assertThat(registrations.count()).isEqualTo(1)
 }
}
