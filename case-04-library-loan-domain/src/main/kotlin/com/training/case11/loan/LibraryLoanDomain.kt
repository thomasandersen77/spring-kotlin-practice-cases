package com.training.case11.loan

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
 TODO("Implement one-time extension with explicit state for whether extension is already used and validate positive day count")
 }

 fun overdueDays(onDate: LocalDate): Long {
 TODO("Implement overdue calculation with zero floor and clear behavior for returned loans")
 }
}
