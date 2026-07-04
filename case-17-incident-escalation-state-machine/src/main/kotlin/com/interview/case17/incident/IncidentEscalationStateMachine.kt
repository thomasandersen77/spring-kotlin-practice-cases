package com.interview.case17.incident

import java.time.Instant

enum class IncidentStatus {
    OPEN,
    ACKNOWLEDGED,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

data class IncidentTransition(val from: IncidentStatus, val to: IncidentStatus, val actor: String, val timestamp: Instant)

class Incident {
    fun acknowledge(actor: String, at: Instant): IncidentTransition {
        TODO("Implement OPEN -> ACKNOWLEDGED")
    }

    fun startWork(actor: String, at: Instant): IncidentTransition {
        TODO("Implement ACKNOWLEDGED -> IN_PROGRESS")
    }

    fun resolve(actor: String, at: Instant): IncidentTransition {
        TODO("Implement IN_PROGRESS -> RESOLVED")
    }

    fun close(actor: String, at: Instant): IncidentTransition {
        TODO("Implement RESOLVED -> CLOSED")
    }

    fun reopen(actor: String, at: Instant): IncidentTransition {
        TODO("Implement RESOLVED -> OPEN")
    }
}
