package com.interview.case17.incident

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class IncidentEscalationStateMachineTest {
    @Test
    fun `open incident should be acknowledged`() {
        val incident = Incident()

        val transition = incident.acknowledge("oncall-1", Instant.parse("2026-01-01T10:00:00Z"))

        assertThat(transition.to).isEqualTo(IncidentStatus.ACKNOWLEDGED)
    }
}
