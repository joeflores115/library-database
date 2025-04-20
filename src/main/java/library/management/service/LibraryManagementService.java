package library.management.service;


import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import library.management.controller.model.LibraryManagementData;
import library.management.controller.model.LibraryManagementData.BookData;
import library.management.controller.model.LibraryManagementData.BorrowerData;
import library.management.controller.model.LibraryManagementData.CheckoutData;
import library.management.dao.LibraryManagementBookDao;
import library.management.dao.LibraryManagementBorrowerDao;
import library.management.dao.LibraryManagementCheckoutDao;
import library.management.dao.LibraryManagementDao;
import library.management.entity.Book;
import library.management.entity.Borrower;
import library.management.entity.Checkout;
import library.management.entity.Libraries;

@Service
public class LibraryManagementService {
	
	@Autowired
	private LibraryManagementDao libraryManagementDao;
	
	@Autowired
	private LibraryManagementBookDao BookDao;
	
	@Autowired
	private LibraryManagementBorrowerDao BorrowerDao;
	
	@Autowired
	private LibraryManagementCheckoutDao CheckoutDao;
	
	//***********************************************	
//Following will be the methods to save a library, book, borrower and checkout
//***********************************************	
	@Transactional(readOnly = false)
	public LibraryManagementData saveLibrary(LibraryManagementData libraryData)
	{
		Libraries library=findOrCreateLibrary(libraryData.getLibraryId());
		
		copyLibraryFields(library, libraryData);
		
		Libraries dbLibrary=libraryManagementDao.save(library);
		
		
		return new LibraryManagementData(dbLibrary);
	}

	@Transactional(readOnly = false)
	private void copyLibraryFields(Libraries library, LibraryManagementData libraryData) 
	{
		library.setLibraryId(libraryData.getLibraryId());
		library.setName(libraryData.getName());
		library.setAddress(libraryData.getAddress());
		library.setCity(libraryData.getCity());
		library.setState(libraryData.getState());
		library.setZip(libraryData.getZip());
		library.setPhone(libraryData.getPhone());
	}

	private Libraries findOrCreateLibrary(Long libraryId) {
		if(Objects.isNull(libraryId))
		{
			
			return new Libraries();
		}
		else
		{
			return findLibraryById(libraryId);
		}
		
	}

	private Libraries findLibraryById(Long libraryId) {
		
		return libraryManagementDao.findById(libraryId).orElseThrow(
				()-> new NoSuchElementException("Library with ID="
						+ libraryId + " not found"));
	}

	@Transactional(readOnly = false)
	public BookData saveBook(Long libraryId, BookData bookData) {
		
		Libraries library = findLibraryById(libraryId);
		Long bookId = bookData.getBookId();
		Book book= findOrCreateBook(bookId);
		copyBookFields(book, bookData);
		book.setLibrary(library);
		library.getBooks().add(book);
		
		Book dbBook = BookDao.save(book);
		
		
		return new BookData(dbBook);
	}

	
	private void copyBookFields(Book book, BookData bookData) 
	{	
		book.setBookId(bookData.getBookId());
		book.setTitle(bookData.getTitle());
		book.setAuthor(bookData.getAuthor());
		book.setIsbn(bookData.getIsbn());
		book.setQuantity(bookData.getQuantity());
	}

	private Book findOrCreateBook(Long bookId) {
		if(Objects.isNull(bookId))
		{
			return new Book();
		}
		else
		{
			return findBookById(bookId);
		}
		
	}

	private Book findBookById(Long bookId) {
		
		return BookDao.findById(bookId).orElseThrow(
				()-> new NoSuchElementException("Book with ID="
						+ bookId + " not found"));
	}

	@Transactional(readOnly = false)
	public BorrowerData saveBorrower(BorrowerData borrowerData) {
		//borrower might be a little different
		//because it is not associated with a library
		//but with a book
		//this method will be to create a borrower for the first time
		//if the already exists we will need to say so
		//and not create a new one
		//we will have another method to update the borrower
		Long borrowerId = borrowerData.getBorrowerId();
		Borrower borrower = findOrCreateBorrower(borrowerId);
		copyBorrowerFields(borrower, borrowerData);
		Borrower dbBorrower = BorrowerDao.save(borrower);
		
		return new BorrowerData(dbBorrower);
	}

	private void copyBorrowerFields(Borrower borrower, BorrowerData borrowerData) {
		borrower.setBorrowerId(borrowerData.getBorrowerId());
		borrower.setName(borrowerData.getName());
		borrower.setAddress(borrowerData.getAddress());
		borrower.setEmail(borrowerData.getEmail());	
	}

	private Borrower findOrCreateBorrower(Long borrowerId) {
		if(Objects.isNull(borrowerId))
		{
			return new Borrower();
		}
		else
		{
			return findBorrowerById(borrowerId);
		}
		
	}
	
	private Borrower findBorrowerById(Long borrowerId) {
		return BorrowerDao.findById(borrowerId).orElseThrow(
				()-> new NoSuchElementException("Borrower with ID="
						+ borrowerId + " not found"));
	}

	@Transactional(readOnly = false)
	public CheckoutData saveCheckout(Long bookId, Long borrowerId, CheckoutData checkoutData) {
		Book book = findBookById(bookId);
		Borrower borrower = findBorrowerById(borrowerId);
		if(book.getQuantity() <= 0)
		{
			throw new IllegalStateException("No copies available");
		}
		book.setQuantity(book.getQuantity() - 1);
		
		Checkout checkout = new Checkout();
		checkout.setBook(book);
		checkout.setBorrower(borrower);
		checkout.setCheckoutDate(LocalDate.now());
		checkout.setDueDate(LocalDate.now().plusDays(14));
		checkout.setReturnDate(null);
		BookDao.save(book);
		Checkout dbCheckout = CheckoutDao.save(checkout);
		return new CheckoutData(dbCheckout);
	}

	
	@Transactional(readOnly = false)
	public CheckoutData returnBook(Long checkoutId) {
		Checkout checkout = CheckoutDao.findById(checkoutId).orElseThrow(
				()-> new NoSuchElementException("Checkout with ID="
						+ checkoutId + " not found"));
		if(checkout.getReturnDate() != null)
		{
			throw new IllegalStateException("Book already returned");
		}
		
		checkout.setReturnDate(LocalDate.now());
		Book book = checkout.getBook();
		book.setQuantity(book.getQuantity() + 1);
		BookDao.save(book);
		Checkout dbCheckout=CheckoutDao.save(checkout);
		
		return new CheckoutData(dbCheckout);
	}

	//Following will be the methods to retrieve a library, book, borrower and checkout
	
	@Transactional(readOnly=true)
	public List<LibraryManagementData> retrieveAllLibraries() {
		List<Libraries> libraryEntities = libraryManagementDao.findAll();
		List<LibraryManagementData> result = new LinkedList<>();
		
		for(Libraries library: libraryEntities)
		{
			LibraryManagementData libraryData = new LibraryManagementData(library);
			result.add(libraryData);
		}
		return result;
	}

	@Transactional(readOnly=true)
	public LibraryManagementData retrieveLibrary(Long libraryId) {
		Libraries library = findLibraryById(libraryId);
		LibraryManagementData libraryData = new LibraryManagementData(library);

		return libraryData;
	}

	@Transactional(readOnly=true)
	public List<BookData> retrieveAllBooks(Long libraryId) {
		List<Book> bookEntities = BookDao.findAll();
		List<BookData> result = new LinkedList<>();
		for(Book book: bookEntities)
		{
			BookData bookData = new BookData(book);
			result.add(bookData);
		}
		return result;
	}

	@Transactional(readOnly=true)
	public BookData retrieveBook(Long libraryId, Long bookId) {
		Book book = findBookById(bookId);
		BookData bookData = new BookData(book);
		return bookData;
	}

	public List<BorrowerData> retrieveAllBorrowers(Long libraryId) {
		List<Borrower> borrowerEntities = BorrowerDao.findAll();
		List<BorrowerData> result = new LinkedList<>();
		for(Borrower borrower: borrowerEntities)
		{
			BorrowerData borrowerData = new BorrowerData(borrower);
			result.add(borrowerData);
		}
		return result;
	}

	public BorrowerData retrieveBorrower(Long libraryId, Long borrowerId) {
		Borrower borrower = findBorrowerById(borrowerId);
		BorrowerData borrowerData = new BorrowerData(borrower);
		return borrowerData;
	}

	public List<CheckoutData> retrieveAllCheckouts(Long libraryId) {
		List<Checkout> checkoutEntities = CheckoutDao.findAll();
		List<CheckoutData> result = new LinkedList<>();
		for(Checkout checkout: checkoutEntities)
		{
			CheckoutData checkoutData = new CheckoutData(checkout);
			result.add(checkoutData);
		}
		return result;
	}

	public CheckoutData retrieveCheckout(Long libraryId, Long checkoutId) {
		Checkout checkout = CheckoutDao.findById(checkoutId).orElseThrow(
				()-> new NoSuchElementException("Checkout with ID="
						+ checkoutId + " not found"));
		CheckoutData checkoutData = new CheckoutData(checkout);
		
		return checkoutData;
	}

	public void deleteLibrary(Long libraryId) {
		Libraries library = findLibraryById(libraryId);
		libraryManagementDao.delete(library);
	}

	public void deleteBook(Long libraryId, Long bookId) {
		Libraries library = findLibraryById(libraryId);
		Book book = findBookById(bookId);
		library.getBooks().remove(book);
		BookDao.delete(book);
	}

	public void deleteBorrower(Long libraryId, Long borrowerId) {
		Borrower borrower = findBorrowerById(borrowerId);
		BorrowerDao.delete(borrower);
	}

	public void deleteCheckout(Long libraryId, Long checkoutId) {
		Checkout checkout = CheckoutDao.findById(checkoutId).orElseThrow(
				()-> new NoSuchElementException("Checkout with ID="
						+ checkoutId + " not found"));
		CheckoutDao.delete(checkout);
	}	
}
