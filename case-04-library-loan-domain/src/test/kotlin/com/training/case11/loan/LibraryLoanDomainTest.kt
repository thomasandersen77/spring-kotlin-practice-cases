package com.training.case11.loan

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LibraryLoanDomainTest {
 @Test
 fun `loan period should require due date after borrow date`() {
 val period = LoanPeriod(
 borrowedAt = LocalDate.parse("2026-01-10"),
 dueAt = LocalDate.parse("2026-01-20")
 )

 assertThat(period.dueAt).isAfter(period.borrowedAt)
 }

 @Test
 fun `active loan should be extendable once`() {
 val loan = Loan(
 bookId = BookId("BOOK-1"),
 borrowerId = BorrowerId("BORROWER-1"),
 period = LoanPeriod(LocalDate.parse("2026-01-10"), LocalDate.parse("2026-01-20")),
 status = LoanStatus.ACTIVE
 )

 val extended = loan.extend(7)

 assertThat(extended.period.dueAt).isEqualTo(LocalDate.parse("2026-01-27"))
 }
}
