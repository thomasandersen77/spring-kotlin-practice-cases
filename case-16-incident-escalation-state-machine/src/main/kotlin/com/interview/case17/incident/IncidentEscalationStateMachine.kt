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
    var status: IncidentStatus = IncidentStatus.OPEN
        private set
    private val mutableHistory = mutableListOf<IncidentTransition>()
    val history: List<IncidentTransition> get() = mutableHistory.toList()

    fun acknowledge(actor: String, at: Instant): IncidentTransition {
        return transition(IncidentStatus.OPEN, IncidentStatus.ACKNOWLEDGED, actor, at)
    }

    fun startWork(actor: String, at: Instant): IncidentTransition {
        return transition(IncidentStatus.ACKNOWLEDGED, IncidentStatus.IN_PROGRESS, actor, at)
    }

    fun resolve(actor: String, at: Instant): IncidentTransition {
        return transition(IncidentStatus.IN_PROGRESS, IncidentStatus.RESOLVED, actor, at)
    }

    fun close(actor: String, at: Instant): IncidentTransition {
        return transition(IncidentStatus.RESOLVED, IncidentStatus.CLOSED, actor, at)
    }

    fun reopen(actor: String, at: Instant): IncidentTransition {
        return transition(IncidentStatus.RESOLVED, IncidentStatus.OPEN, actor, at)
    }

    private fun transition(
        expected: IncidentStatus,
        target: IncidentStatus,
        actor: String,
        at: Instant
    ): IncidentTransition {
        require(actor.isNotBlank()) { "actor cannot be blank" }
        check(status == expected) { "cannot transition from $status to $target" }
        mutableHistory.lastOrNull()?.let {
            require(at.isAfter(it.timestamp)) { "transition timestamp must be after previous transition" }
        }

        return IncidentTransition(status, target, actor, at).also {
            status = target
            mutableHistory += it
        }
    }
}
