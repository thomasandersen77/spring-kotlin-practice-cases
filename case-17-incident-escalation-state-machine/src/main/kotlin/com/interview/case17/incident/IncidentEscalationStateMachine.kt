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
        TODO("Implement validated transition OPEN -> ACKNOWLEDGED and persist current status")
    }

    fun startWork(actor: String, at: Instant): IncidentTransition {
        TODO("Implement validated transition ACKNOWLEDGED -> IN_PROGRESS")
    }

    fun resolve(actor: String, at: Instant): IncidentTransition {
        TODO("Implement validated transition IN_PROGRESS -> RESOLVED")
    }

    fun close(actor: String, at: Instant): IncidentTransition {
        TODO("Implement validated transition RESOLVED -> CLOSED")
    }

    fun reopen(actor: String, at: Instant): IncidentTransition {
        TODO("Implement validated transition RESOLVED -> OPEN and reject invalid reopen paths")
    }
}
