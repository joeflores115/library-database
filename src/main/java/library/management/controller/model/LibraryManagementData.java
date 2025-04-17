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
	
	
	
	
	@Data
	@NoArgsConstructor
	public static class LibraryManagementBooks{
		private Long bookId;
		private String title;
		private String author;
		private String isbn;
		private int quantity;
		
	}
	
	@Data
	@NoArgsConstructor
	public static class LibraryManagementBorrowers{
		private Long borrowerId;
		private String name;
		private String address;
		private String email;
		
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
			
			for (Borrower borrower : book.getBorrowers())
			{
				LibraryManagementBorrowers libraryManagementBorrowers = new LibraryManagementBorrowers();
				libraryManagementBorrowers.setBorrowerId(borrower.getBorrowerId());
				libraryManagementBorrowers.setName(borrower.getName());
				libraryManagementBorrowers.setAddress(borrower.getAddress());
				libraryManagementBorrowers.setEmail(borrower.getEmail());
				borrowers.add(libraryManagementBorrowers);
			}
			
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
