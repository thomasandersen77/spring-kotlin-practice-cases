package com.training.case47.registrations

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@SpringBootApplication
class RegistrationApplication

fun main(args: Array<String>) {
 runApplication<RegistrationApplication>(*args)
}

@Entity
@Table(name = "events")
class EventEntity(
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
 var id: Long? = null,
 @Column(nullable = false)
 var name: String = "",
 @Column(nullable = false)
 var availableSeats: Int = 0
) {
 // TODO 1: Reserver én plass uten å tillate negativ kapasitet.
 fun reserveSeat() {
 TODO("Beskytt kapasitetens invariant")
 }
}

@Entity
@Table(
 name = "registrations",
 uniqueConstraints = [UniqueConstraint(columnNames = ["event_id", "email"])]
)
class RegistrationEntity(
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
 var id: Long? = null,
 @Column(name = "event_id", nullable = false)
 var eventId: Long = 0,
 @Column(nullable = false)
 var email: String = ""
)

interface EventRepository : JpaRepository<EventEntity, Long>
interface RegistrationRepository : JpaRepository<RegistrationEntity, Long> {
 fun existsByEventIdAndEmail(eventId: Long, email: String): Boolean
}

data class RegistrationReceipt(val registrationId: Long, val eventId: Long, val remainingSeats: Int)

class EventNotFound(id: Long) : RuntimeException("Arrangement $id finnes ikke")
class NoSeatsAvailable : RuntimeException("Ingen ledige plasser")
class AlreadyRegistered(email: String) : RuntimeException("$email er allerede registrert")

@Service
class RegistrationService(
 private val events: EventRepository,
 private val registrations: RegistrationRepository
) {
 // TODO 2: Definer én transaksjonsgrense for kapasitet og registrering.
 // TODO 3: Ved exception skal begge databaseendringer rulles tilbake.
 @Transactional
 fun register(eventId: Long, email: String): RegistrationReceipt =
 TODO("Implementer atomisk påmelding")
}
