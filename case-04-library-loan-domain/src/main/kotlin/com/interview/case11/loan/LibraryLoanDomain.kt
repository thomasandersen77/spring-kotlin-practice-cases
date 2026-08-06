package com.interview.case11.loan

import java.time.LocalDate
import java.time.temporal.ChronoUnit

@JvmInline
value class BookId(val value: String) {
    init { require(value.isNotBlank()) { "book id cannot be blank" } }
}

@JvmInline
value class BorrowerId(val value: String) {
    init { require(value.isNotBlank()) { "borrower id cannot be blank" } }
}

enum class LoanStatus {
    ACTIVE,
    RETURNED
}

data class LoanPeriod(val borrowedAt: LocalDate, val dueAt: LocalDate) {
    init {
        require(dueAt.isAfter(borrowedAt)) { "due date must be after borrowed date" }
    }
}

class Loan(
    val bookId: BookId,
    val borrowerId: BorrowerId,
    val period: LoanPeriod,
    val status: LoanStatus,
    val extensionUsed: Boolean = false
) {
    fun extend(days: Long): Loan {
        check(status == LoanStatus.ACTIVE) { "only active loans can be extended" }
        check(!extensionUsed) { "loan has already been extended" }
        require(days in 1..MAX_EXTENSION_DAYS) { "extension must be between 1 and $MAX_EXTENSION_DAYS days" }

        return Loan(bookId, borrowerId, period.copy(dueAt = period.dueAt.plusDays(days)), status, true)
    }

    fun overdueDays(onDate: LocalDate): Long {
        if (status == LoanStatus.RETURNED || !onDate.isAfter(period.dueAt)) return 0
        return ChronoUnit.DAYS.between(period.dueAt, onDate)
    }

    private companion object {
        const val MAX_EXTENSION_DAYS = 30L
    }
}
