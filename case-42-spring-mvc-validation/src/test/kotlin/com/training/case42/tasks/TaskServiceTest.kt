package com.training.case42.tasks

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TaskServiceTest {

    @Test
    fun `service normaliserer tittel og returnerer opprettet oppgave`() {
        val response =
            TaskService().create(CreateTaskRequest(" Forbered trening ", TaskPriority.HIGH))

        assertThat(response.id).isPositive()
        assertThat(response.title).isEqualTo("Forbered trening")
        assertThat(response.priority).isEqualTo(TaskPriority.HIGH)
    }

    @Test
    fun `when title exceeds 80 chars IllegalArgrumentException is throwm`() {
        assertThrows<IllegalArgumentException> {
            val title =
                """title kan ikke være lengre enn 80 tegn. " +
					"Men denne titlen er mye lengre. Den er mye, mye, mye lengre."""

            TaskService().normalizeTitle(CreateTaskRequest(title, TaskPriority.HIGH))
        }
    }

    @Test
    fun `title can be 80 chars or below`() {
        val normalizedTitle = TaskService().normalizeTitle(
            CreateTaskRequest(
                TITLE_MAX_LENGTH_ERROR_MESSAGE,
                TaskPriority.HIGH
            )
        )

        assertThat(normalizedTitle).isEqualTo(TITLE_MAX_LENGTH_ERROR_MESSAGE)
    }
}
