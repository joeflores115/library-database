package library.management.controller.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import library.management.entity.Book;
import library.management.entity.Borrower;
import library.management.entity.Checkout;
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
	public static class CheckoutData
	{
		private Long checkoutId;
	    
	    private String title;
	    private String author;
	    private String isbn;
	    private int quantity;
	    
	    private String name;
	    private String address;
	    private String email;
	    private LocalDate checkoutDate;
	    private LocalDate dueDate;
	    private LocalDate returnDate;
	
	    public CheckoutData(Checkout checkout) {
	        this.checkoutId = checkout.getCheckoutId();
	        this.title = checkout.getBook().getTitle();
	        this.author = checkout.getBook().getAuthor();
	        this.isbn = checkout.getBook().getIsbn();
	        this.quantity = checkout.getBook().getQuantity();
	        this.name = checkout.getBorrower().getName();
	        this.address = checkout.getBorrower().getAddress();
	        this.email = checkout.getBorrower().getEmail();
	        this.checkoutDate = checkout.getCheckoutDate();
	        this.dueDate = checkout.getDueDate();
	        this.returnDate = checkout.getReturnDate();
	    }		
	    public CheckoutData(Book book, Long borrowerId, LocalDate dueDate, LocalDate returnDate)
		{

			this.title = book.getTitle();
			this.author = book.getAuthor();
			this.isbn = book.getIsbn();
			this.quantity = book.getQuantity();

			this.dueDate = dueDate;
			this.returnDate = returnDate;
		}
	    
	    public Checkout toCheckout()
	    {
	    	Checkout checkout = new Checkout();
	    	checkout.setCheckoutId(checkoutId);
	    	checkout.setCheckoutDate(checkoutDate);
	    	checkout.setDueDate(dueDate);
	    	checkout.setReturnDate(returnDate);
	    	
	    	return checkout;
	    }
	
	}
	
	@Data
	@NoArgsConstructor
	public static class BookData
	{
		private Long bookId;
		private String title;
		private String author;
		private String isbn;
		private int quantity;
		
		public BookData(Book book)
		{
			this.bookId = book.getBookId();
			this.title = book.getTitle();
			this.author = book.getAuthor();
			this.isbn = book.getIsbn();
			this.quantity = book.getQuantity();
		
		}
		
		public Book toBook()
		{
			Book book = new Book();
			book.setBookId(bookId);
			book.setTitle(title);
			book.setAuthor(author);
			book.setIsbn(isbn);
			book.setQuantity(quantity);
			
			return book;
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class BorrowerData
	{
		private Long borrowerId;
		private String name;
		private String address;
		private String email;
		
		public BorrowerData(Borrower borrower)
		{
			this.borrowerId = borrower.getBorrowerId();
			this.name = borrower.getName();
			this.address = borrower.getAddress();
			this.email = borrower.getEmail();
			
		}
		
		public Borrower toBorrower()
		{
			Borrower borrower = new Borrower();
			borrower.setBorrowerId(borrowerId);
			borrower.setName(name);
			borrower.setAddress(address);
			borrower.setEmail(email);
			
			return borrower;
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
}
