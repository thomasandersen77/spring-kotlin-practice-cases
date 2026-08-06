package com.interview.case31.hrmapping

import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import java.time.LocalDate

class EmployeeMappingContractTest {

    @Test
    fun `negative top skill count is rejected also for empty input`() {
        assertThatIllegalArgumentException()
            .isThrownBy {
                emptyList<EmployeeEntity>().toDepartmentSummaries(
                    today = LocalDate.of(2026, 8, 3),
                    topSkillCount = -1
                )
            }
            .withMessage("topSkillCount cannot be negative")
    }
}
