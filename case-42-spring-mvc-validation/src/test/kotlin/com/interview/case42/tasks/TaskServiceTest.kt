package com.interview.case42.tasks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TaskServiceTest {

    @Test
    fun `service normaliserer tittel og returnerer opprettet oppgave`() {
        val response = TaskService().create(CreateTaskRequest("  Forbered intervju  ", TaskPriority.HIGH))

        assertThat(response.id).isPositive()
        assertThat(response.title).isEqualTo("Forbered intervju")
        assertThat(response.priority).isEqualTo(TaskPriority.HIGH)
    }
}
