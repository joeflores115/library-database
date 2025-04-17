package library.management.controller.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import library.management.entity.Book;
import library.management.entity.Borrower;
import library.management.entity.Libraries;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class LibraryManagementData {

	private Long libraryId;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	
	private Set<LibraryManagementBooks> books = new HashSet<>();
	
	
	public LibraryManagementData(Libraries library)
	{
		libraryId= library.getLibraryId();
		name= library.getName();
		address= library.getAddress();
		city= library.getCity();
		state= library.getState();
		zip= library.getZip();
		phone= library.getPhone();
		
		for (Book book : library.getBooks())
		{
			LibraryManagementBooks libraryManagementBooks = new LibraryManagementBooks();
			libraryManagementBooks.setBookId(book.getBookId());
			libraryManagementBooks.setTitle(book.getTitle());
			libraryManagementBooks.setAuthor(book.getAuthor());
			libraryManagementBooks.setIsbn(book.getIsbn());
			libraryManagementBooks.setQuantity(book.getQuantity());
			books.add(libraryManagementBooks);
		}
		
	}
	
	public LibraryManagementData(Long libraryId, String name, String address, String city, String state, String zip,
			String phone) {
		this.libraryId = libraryId;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phone = phone;
	}
	
	public Libraries toLibrary() {
		Libraries library = new Libraries();
		
		library.setLibraryId(libraryId);
		library.setName(name);
		library.setAddress(address);
		library.setCity(city);
		library.setState(state);
		library.setZip(zip);
		library.setPhone(phone);
		
		for (LibraryManagementBooks libraryManagementBooks : books)
		{
			Book book = new Book();
			book.setBookId(libraryManagementBooks.getBookId());
			book.setTitle(libraryManagementBooks.getTitle());
			book.setAuthor(libraryManagementBooks.getAuthor());
			book.setIsbn(libraryManagementBooks.getIsbn());
			book.setQuantity(libraryManagementBooks.getQuantity());
			library.getBooks().add(book);
		}
		
		return library;
	}
	
	
	
	@Data
	@NoArgsConstructor
	public static class LibraryManagementBooks{
		private Long bookId;
		private String title;
		private String author;
		private String isbn;
		private int quantity;
		public LibraryManagementBooks(Book book)
		{
			bookId = book.getBookId();
			title = book.getTitle();
			author = book.getAuthor();
			isbn = book.getIsbn();
			quantity = book.getQuantity();
		}
		
	}
	
	@Data
	@NoArgsConstructor
	public static class LibraryManagementBorrowers{
		private Long borrowerId;
		private String name;
		private String address;
		private String email;
		
		public LibraryManagementBorrowers(Borrower borrower)
		{
			borrowerId = borrower.getBorrowerId();
			name = borrower.getName();
			address = borrower.getAddress();
			email = borrower.getEmail();
		}
		
	}
	
	@Data
	@NoArgsConstructor
	public class BooksData
	{
		private Long bookId;
		private String title;
		private String author;
		private String isbn;
		private int quantity;
		private Set<LibraryManagementBorrowers> borrowers = new HashSet<>();
		public BooksData(Book book)
		{
			this.bookId = book.getBookId();
			this.title = book.getTitle();
			this.author = book.getAuthor();
			this.isbn = book.getIsbn();
			this.quantity = book.getQuantity();
			
		}
		
		public BooksData(Long bookId, String title, String author, String isbn, int quantity) {
			this.bookId = bookId;
			this.title = title;
			this.author = author;
			this.isbn = isbn;
			this.quantity = quantity;
		}
		
		public Book toBook()
		{
			Book book = new Book();
			book.setBookId(bookId);
			book.setTitle(title);
			book.setAuthor(author);
			book.setIsbn(isbn);
			book.setQuantity(quantity);
			
			for (LibraryManagementBorrowers libraryManagementBorrowers : borrowers)
			{
				Borrower borrower = new Borrower();
				borrower.setBorrowerId(libraryManagementBorrowers.getBorrowerId());
				borrower.setName(libraryManagementBorrowers.getName());
				borrower.setAddress(libraryManagementBorrowers.getAddress());
				borrower.setEmail(libraryManagementBorrowers.getEmail());
				book.getBorrowers().add(borrower);
			}
			
			return book;
		}
		
		
	}
}
