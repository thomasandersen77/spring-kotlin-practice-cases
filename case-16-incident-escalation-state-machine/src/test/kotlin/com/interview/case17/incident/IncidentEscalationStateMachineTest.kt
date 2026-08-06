package com.interview.case17.incident

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Instant

class IncidentEscalationStateMachineTest {
    @Test
    fun `open incident should be acknowledged`() {
        val incident = Incident()

        val transition = incident.acknowledge("oncall-1", Instant.parse("2026-01-01T10:00:00Z"))

        assertThat(transition.to).isEqualTo(IncidentStatus.ACKNOWLEDGED)
        assertThat(incident.status).isEqualTo(IncidentStatus.ACKNOWLEDGED)
        assertThat(incident.history).containsExactly(transition)
    }

    @Test
    fun `incident should follow complete lifecycle`() {
        val incident = Incident()
        val start = Instant.parse("2026-01-01T10:00:00Z")

        incident.acknowledge("oncall", start)
        incident.startWork("oncall", start.plusSeconds(1))
        incident.resolve("resolver", start.plusSeconds(2))
        val closed = incident.close("owner", start.plusSeconds(3))

        assertThat(closed.from).isEqualTo(IncidentStatus.RESOLVED)
        assertThat(incident.status).isEqualTo(IncidentStatus.CLOSED)
        assertThat(incident.history).hasSize(4)
    }

    @Test
    fun `resolved incident can reopen and start a new lifecycle`() {
        val incident = Incident()
        val start = Instant.parse("2026-01-01T10:00:00Z")
        incident.acknowledge("a", start)
        incident.startWork("a", start.plusSeconds(1))
        incident.resolve("a", start.plusSeconds(2))

        val reopened = incident.reopen("b", start.plusSeconds(3))

        assertThat(reopened.to).isEqualTo(IncidentStatus.OPEN)
        incident.acknowledge("b", start.plusSeconds(4))
        assertThat(incident.status).isEqualTo(IncidentStatus.ACKNOWLEDGED)
    }

    @Test
    fun `invalid transitions actor and timestamp should fail without state change`() {
        val incident = Incident()
        val start = Instant.parse("2026-01-01T10:00:00Z")

        assertThatThrownBy { incident.close("actor", start) }.isInstanceOf(IllegalStateException::class.java)
        assertThatThrownBy { incident.acknowledge(" ", start) }.isInstanceOf(IllegalArgumentException::class.java)
        incident.acknowledge("actor", start)
        assertThatThrownBy { incident.startWork("actor", start) }
            .isInstanceOf(IllegalArgumentException::class.java)
        assertThat(incident.status).isEqualTo(IncidentStatus.ACKNOWLEDGED)
    }
}
