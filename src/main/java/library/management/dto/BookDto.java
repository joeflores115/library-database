package library.management.dto;

import library.management.entity.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
	@NotBlank(message = "ISBN is required")
	private String isbn;
	@Min(value = 0, message = "Quantity cannot be negative")
	private int quantity;

	public BookDto(Book book) {
		this.bookId = book.getBookId();
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.isbn = book.getIsbn();
		this.quantity = book.getQuantity();
	}
}
