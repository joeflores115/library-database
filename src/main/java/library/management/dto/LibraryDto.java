package library.management.dto;

import java.util.HashSet;
import java.util.Set;
import library.management.entity.Book;
import library.management.entity.Library;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LibraryDto {
	private Long libraryId;
	@NotBlank(message = "Name is required")
	private String name;
	@NotBlank(message = "Address is required")
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private Set<BookDto> books = new HashSet<>();

	public LibraryDto(Library library) {
		this.libraryId = library.getLibraryId();
		this.name = library.getName();
		this.address = library.getAddress();
		this.city = library.getCity();
		this.state = library.getState();
		this.zip = library.getZip();
		this.phone = library.getPhone();
		if (library.getBooks() != null) {
			for (Book book : library.getBooks()) {
				this.books.add(new BookDto(book));
			}
		}
	}
}
