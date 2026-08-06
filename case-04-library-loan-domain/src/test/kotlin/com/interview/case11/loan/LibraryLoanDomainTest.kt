package com.interview.case11.loan

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
        assertThat(extended.extensionUsed).isTrue()
        assertThat(loan.extensionUsed).isFalse()
    }

    @Test
    fun `loan cannot be extended twice`() {
        val loan = activeLoan().extend(7)

        assertThatThrownBy { loan.extend(1) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("already")
    }

    @Test
    fun `returned loan and invalid extension length cannot be extended`() {
        val returned = Loan(
            BookId("BOOK-1"), BorrowerId("BORROWER-1"),
            LoanPeriod(LocalDate.parse("2026-01-10"), LocalDate.parse("2026-01-20")),
            LoanStatus.RETURNED
        )

        assertThatThrownBy { returned.extend(7) }.isInstanceOf(IllegalStateException::class.java)
        assertThatThrownBy { activeLoan().extend(0) }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { activeLoan().extend(31) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `overdue days should have zero floor and ignore returned loans`() {
        val active = activeLoan()
        val returned = Loan(active.bookId, active.borrowerId, active.period, LoanStatus.RETURNED)

        assertThat(active.overdueDays(LocalDate.parse("2026-01-19"))).isZero()
        assertThat(active.overdueDays(LocalDate.parse("2026-01-20"))).isZero()
        assertThat(active.overdueDays(LocalDate.parse("2026-01-23"))).isEqualTo(3)
        assertThat(returned.overdueDays(LocalDate.parse("2026-01-23"))).isZero()
    }

    private fun activeLoan() = Loan(
        bookId = BookId("BOOK-1"),
        borrowerId = BorrowerId("BORROWER-1"),
        period = LoanPeriod(LocalDate.parse("2026-01-10"), LocalDate.parse("2026-01-20")),
        status = LoanStatus.ACTIVE
    )
}
