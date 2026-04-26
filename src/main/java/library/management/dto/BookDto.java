package library.management.dto;

import library.management.entity.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDto {
	private Long bookId;
	@NotBlank(message = "Title is required")
	private String title;
	@NotBlank(message = "Author is required")
	private String author;
	private String isbn;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;
    private String ownerName;
    private boolean available;
    private String borrowerName;

	public BookDto(Book book) {
		this.bookId = book.getBookId();
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.isbn = book.getIsbn();
        if (book.getOwner() != null) {
            this.ownerId = book.getOwner().getMemberId();
            this.ownerName = book.getOwner().getName();
        }
        this.available = book.getLoans().stream()
                .allMatch(loan -> loan.getReturnDate() != null);

        book.getLoans().stream()
            .filter(l -> l.getReturnDate() == null)
            .findFirst()
            .ifPresent(l -> this.borrowerName = l.getBorrower().getName());
	}
}
