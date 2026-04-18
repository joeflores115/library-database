package library.management.dto;

import java.time.LocalDate;
import library.management.entity.Checkout;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckoutDto {
	private Long checkoutId;
	private Long bookId;
	private Long borrowerId;
	private String bookTitle;
	private String borrowerName;
	private LocalDate checkoutDate;
	private LocalDate dueDate;
	private LocalDate returnDate;

	public CheckoutDto(Checkout checkout) {
		this.checkoutId = checkout.getCheckoutId();
		if (checkout.getBook() != null) {
			this.bookId = checkout.getBook().getBookId();
			this.bookTitle = checkout.getBook().getTitle();
		}
		if (checkout.getBorrower() != null) {
			this.borrowerId = checkout.getBorrower().getBorrowerId();
			this.borrowerName = checkout.getBorrower().getName();
		}
		this.checkoutDate = checkout.getCheckoutDate();
		this.dueDate = checkout.getDueDate();
		this.returnDate = checkout.getReturnDate();
	}
}
