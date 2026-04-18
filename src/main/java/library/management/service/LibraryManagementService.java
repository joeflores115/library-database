package library.management.service;


import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library.management.exception.NoCopiesAvailableException;
import library.management.dto.LibraryDto;
import library.management.dto.BookDto;
import library.management.dto.BorrowerDto;
import library.management.dto.CheckoutDto;
import library.management.repository.BookRepository;
import library.management.repository.BorrowerRepository;
import library.management.repository.CheckoutRepository;
import library.management.repository.LibraryRepository;
import library.management.entity.Book;
import library.management.entity.Borrower;
import library.management.entity.Checkout;
import library.management.entity.Library;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LibraryManagementService {
	
	@Autowired
	private LibraryRepository libraryRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private BorrowerRepository borrowerRepository;
	
	@Autowired
	private CheckoutRepository checkoutRepository;
	
	@Transactional
	public LibraryDto saveLibrary(LibraryDto libraryData)
	{
		Library library=findOrCreateLibrary(libraryData.getLibraryId());
		
		copyLibraryFields(library, libraryData);
		
		Library dbLibrary=libraryRepository.save(library);
		
		
		return new LibraryDto(dbLibrary);
	}

	@Transactional
	private void copyLibraryFields(Library library, LibraryDto libraryData)
	{
		library.setLibraryId(libraryData.getLibraryId());
		library.setName(libraryData.getName());
		library.setAddress(libraryData.getAddress());
		library.setCity(libraryData.getCity());
		library.setState(libraryData.getState());
		library.setZip(libraryData.getZip());
		library.setPhone(libraryData.getPhone());
	}

	private Library findOrCreateLibrary(Long libraryId) {
		if(Objects.isNull(libraryId))
		{
			
			return new Library();
		}
		else
		{
			return findLibraryById(libraryId);
		}
		
	}

	private Library findLibraryById(Long libraryId) {
		
		return libraryRepository.findById(libraryId).orElseThrow(
				()-> new NoSuchElementException("Library with ID="
						+ libraryId + " not found"));
	}

	@Transactional
	public BookDto saveBook(Long libraryId, BookDto bookData) {
		
		Library library = findLibraryById(libraryId);
		Long bookId = bookData.getBookId();
		Book book= findOrCreateBook(bookId);
		copyBookFields(book, bookData);
		book.setLibrary(library);
		library.getBooks().add(book);
		
		Book dbBook = bookRepository.save(book);
		
		
		return new BookDto(dbBook);
	}

	
	private void copyBookFields(Book book, BookDto bookData)
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
		
		return bookRepository.findById(bookId).orElseThrow(
				()-> new NoSuchElementException("Book with ID="
						+ bookId + " not found"));
	}

	@Transactional
	public BorrowerDto saveBorrower(BorrowerDto borrowerData) {
		Long borrowerId = borrowerData.getBorrowerId();
		Borrower borrower = findOrCreateBorrower(borrowerId);
		copyBorrowerFields(borrower, borrowerData);
		Borrower dbBorrower = borrowerRepository.save(borrower);
		
		return new BorrowerDto(dbBorrower);
	}

	private void copyBorrowerFields(Borrower borrower, BorrowerDto borrowerData) {
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
		return borrowerRepository.findById(borrowerId).orElseThrow(
				()-> new NoSuchElementException("Borrower with ID="
						+ borrowerId + " not found"));
	}

	@Transactional
	public CheckoutDto saveCheckout(Long bookId, Long borrowerId) {
		Book book = findBookById(bookId);
		Borrower borrower = findBorrowerById(borrowerId);
		if(book.getQuantity() <= 0)
		{
			throw new NoCopiesAvailableException("No copies available for book: " + book.getTitle());
		}
		book.setQuantity(book.getQuantity() - 1);
		
		Checkout checkout = new Checkout();
		checkout.setBook(book);
		checkout.setBorrower(borrower);
		checkout.setCheckoutDate(LocalDate.now());
		checkout.setDueDate(LocalDate.now().plusDays(14));
		checkout.setReturnDate(null);
		bookRepository.save(book);
		Checkout dbCheckout = checkoutRepository.save(checkout);
		return new CheckoutDto(dbCheckout);
	}

	
	@Transactional
	public CheckoutDto updateCheckout(Long checkoutId, CheckoutDto checkoutData) {
		Checkout checkout = checkoutRepository.findById(checkoutId).orElseThrow(
				()-> new NoSuchElementException("Checkout with ID="
						+ checkoutId + " not found"));

		if (checkoutData.getReturnDate() != null && checkout.getReturnDate() == null) {
			checkout.setReturnDate(checkoutData.getReturnDate());
			Book book = checkout.getBook();
			book.setQuantity(book.getQuantity() + 1);
			bookRepository.save(book);
		} else if (checkoutData.getReturnDate() == null && checkout.getReturnDate() != null) {
			Book book = checkout.getBook();
			if (book.getQuantity() <= 0) {
				throw new NoCopiesAvailableException("Cannot undo return: No copies available for book: " + book.getTitle());
			}
			checkout.setReturnDate(null);
			book.setQuantity(book.getQuantity() - 1);
			bookRepository.save(book);
		}

		if (checkoutData.getDueDate() != null) {
			checkout.setDueDate(checkoutData.getDueDate());
		}

		Checkout dbCheckout = checkoutRepository.save(checkout);
		return new CheckoutDto(dbCheckout);
	}

	@Transactional
	public CheckoutDto returnBook(Long checkoutId) {
		Checkout checkout = checkoutRepository.findById(checkoutId).orElseThrow(
				()-> new NoSuchElementException("Checkout with ID="
						+ checkoutId + " not found"));
		if(checkout.getReturnDate() != null)
		{
			throw new IllegalStateException("Book already returned");
		}
		
		checkout.setReturnDate(LocalDate.now());
		Book book = checkout.getBook();
		book.setQuantity(book.getQuantity() + 1);
		bookRepository.save(book);
		Checkout dbCheckout=checkoutRepository.save(checkout);
		
		return new CheckoutDto(dbCheckout);
	}

	
	@Transactional(readOnly=true)
	public List<LibraryDto> retrieveAllLibraries() {
		List<Library> libraryEntities = libraryRepository.findAll();
		List<LibraryDto> result = new LinkedList<>();
		
		for(Library library: libraryEntities)
		{
			LibraryDto libraryData = new LibraryDto(library);
			result.add(libraryData);
		}
		return result;
	}

	@Transactional(readOnly=true)
	public LibraryDto retrieveLibrary(Long libraryId) {
		Library library = findLibraryById(libraryId);
		LibraryDto libraryData = new LibraryDto(library);

		return libraryData;
	}

	@Transactional(readOnly=true)
	public List<BookDto> retrieveAllBooks(Long libraryId) {
		List<Book> bookEntities = bookRepository.findAllByLibraryLibraryId(libraryId);
		List<BookDto> result = new LinkedList<>();
		for(Book book: bookEntities)
		{
			BookDto bookData = new BookDto(book);
			result.add(bookData);
		}
		return result;
	}
	
	@Transactional(readOnly=true)
	public BookDto retrieveBook(Long libraryId, Long bookId) {
		Book book = findBookById(bookId);
		BookDto bookData = new BookDto(book);
		return bookData;
	}
	@Transactional(readOnly=true)
	public List<BorrowerDto> retrieveAllBorrowers() {
		List<Borrower> borrowerEntities = borrowerRepository.findAll();
		List<BorrowerDto> result = new LinkedList<>();
		for(Borrower borrower: borrowerEntities)
		{
			BorrowerDto borrowerData = new BorrowerDto(borrower);
			result.add(borrowerData);
		}
		return result;
	}
	@Transactional(readOnly=true)
	public BorrowerDto retrieveBorrower(Long borrowerId) {
		Borrower borrower = findBorrowerById(borrowerId);
		BorrowerDto borrowerData = new BorrowerDto(borrower);
		return borrowerData;
	}
	@Transactional(readOnly=true)
	public List<CheckoutDto> retrieveAllCheckouts(Long libraryId) {
		List<Checkout> checkoutEntities = checkoutRepository.findAllByBookLibraryLibraryId(libraryId);
		List<CheckoutDto> result = new LinkedList<>();
		for(Checkout checkout: checkoutEntities)
		{
			CheckoutDto checkoutData = new CheckoutDto(checkout);
			result.add(checkoutData);
		}
		return result;
	}
	@Transactional(readOnly=true)
	public CheckoutDto retrieveCheckout(Long libraryId, Long checkoutId) {
		Checkout checkout = checkoutRepository.findById(checkoutId).orElseThrow(
				()-> new NoSuchElementException("Checkout with ID="
						+ checkoutId + " not found"));
		CheckoutDto checkoutData = new CheckoutDto(checkout);
		
		return checkoutData;
	}
	@Transactional
	public void deleteLibrary(Long libraryId) {
		 Library library = findLibraryById(libraryId);
		    log.info("Deleting library: {} with {} books", libraryId, library.getBooks().size());

		    libraryRepository.delete(library);
		    log.info("Library {} deleted", libraryId);
	}
	@Transactional
	public void deleteBook(Long libraryId, Long bookId) {
		Library library = findLibraryById(libraryId);
		Book book = findBookById(bookId);
		library.getBooks().remove(book);
		bookRepository.delete(book);
	}
	@Transactional
	public void deleteBorrower(Long borrowerId) {
		Borrower borrower = findBorrowerById(borrowerId);
		borrowerRepository.delete(borrower);
	}
	@Transactional
	public void deleteCheckout(Long checkoutId) {
		Checkout checkout = checkoutRepository.findById(checkoutId).orElseThrow(
				()-> new NoSuchElementException("Checkout with ID="
						+ checkoutId + " not found"));
		checkoutRepository.delete(checkout);
	}	
}
