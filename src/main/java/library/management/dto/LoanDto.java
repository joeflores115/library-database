package library.management.dto;

import java.time.LocalDate;
import library.management.entity.Loan;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanDto {
	private Long loanId;

    @NotNull(message = "Book ID is required")
	private Long bookId;

    @NotNull(message = "Borrower ID is required")
	private Long borrowerId;

	private String bookTitle;
	private String borrowerName;
	private LocalDate borrowDate;
	private LocalDate dueDate;
	private LocalDate returnDate;

	public LoanDto(Loan loan) {
		this.loanId = loan.getLoanId();
		if (loan.getBook() != null) {
			this.bookId = loan.getBook().getBookId();
			this.bookTitle = loan.getBook().getTitle();
		}
		if (loan.getBorrower() != null) {
			this.borrowerId = loan.getBorrower().getMemberId();
			this.borrowerName = loan.getBorrower().getName();
		}
		this.borrowDate = loan.getBorrowDate();
		this.dueDate = loan.getDueDate();
		this.returnDate = loan.getReturnDate();
	}
}
