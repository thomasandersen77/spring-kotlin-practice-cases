package com.interview.case11.loan

import java.time.LocalDate

@JvmInline
value class BookId(val value: String)

@JvmInline
value class BorrowerId(val value: String)

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
    val status: LoanStatus
) {
    fun extend(days: Long): Loan {
        TODO("Implement one-time extension rules")
    }

    fun overdueDays(onDate: LocalDate): Long {
        TODO("Implement overdue day calculation")
    }
}
