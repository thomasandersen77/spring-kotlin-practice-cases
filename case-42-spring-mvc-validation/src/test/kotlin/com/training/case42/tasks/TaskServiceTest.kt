package com.training.case42.tasks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TaskServiceTest {

    @Test
    fun `service normaliserer tittel og returnerer opprettet oppgave`() {
        val response =
            TaskService().create(CreateTaskRequest(" Forbered trening ", TaskPriority.HIGH))

        assertThat(response.id).isPositive()
        assertThat(response.title).isEqualTo("Forbered trening")
        assertThat(response.priority).isEqualTo(TaskPriority.HIGH)
    }
}
