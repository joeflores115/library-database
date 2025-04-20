package library.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import library.management.controller.model.LibraryManagementData;
import library.management.controller.model.LibraryManagementData.BookData;
import library.management.controller.model.LibraryManagementData.BorrowerData;
import library.management.controller.model.LibraryManagementData.CheckoutData;
import library.management.service.LibraryManagementService;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/library")
@RestController
@Slf4j
public class LibraryManagementController
{
	@Autowired
	LibraryManagementService libraryManagementService;
	
	//*********************************************
	//The following methods are to Create
	//*********************************************
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public LibraryManagementData createLibrary(@RequestBody LibraryManagementData libraryData)
	{
		log.info("Creating library: {}", libraryData);
		
		return libraryManagementService.saveLibrary(libraryData);
	}
	
	
	
	@PostMapping("/{libraryId}/book")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BookData insertBook(@PathVariable Long libraryId, @RequestBody BookData bookData)
	{
		log.info("Creating book: {}", bookData);
		
		return libraryManagementService.saveBook(libraryId, bookData);
	}
	
	
	@PostMapping("/{libraryId}/book/borrower")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BorrowerData insertBorrower(@PathVariable Long libraryId, @RequestBody BorrowerData borrowerData)
	{
		log.info("Creating borrower: {}", borrowerData);
		
		return libraryManagementService.saveBorrower(borrowerData);
	}
	
	
	@PostMapping("/book/{bookId}/checkout/{borrowerId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CheckoutData bookCheckout(@PathVariable Long bookId, @PathVariable Long borrowerId ,@RequestBody CheckoutData checkoutData)
	{
		log.info("Creating checkout: {}", checkoutData);
		
		return libraryManagementService.saveCheckout(bookId, borrowerId ,checkoutData);
	}
	
	@PostMapping("/book/{bookId}/checkout/{checkoutId}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CheckoutData bookReturn(@PathVariable Long checkoutId ,@RequestBody CheckoutData checkoutData)
	{
		log.info("Returning book: {}", checkoutData);
		
		return libraryManagementService.returnBook(checkoutId);
	}
	
	//********************************************
	//The following methods will be used Read
	//********************************************
	
	@GetMapping
	public List<LibraryManagementData> retrieveAllLibraries()
	{
		log.info("Retrieving all libraries");
		
		return libraryManagementService.retrieveAllLibraries();
	}
	
	@GetMapping("/{libraryId}")
	public LibraryManagementData retrieveLibrary(@PathVariable Long libraryId)
	{
		log.info("Retrieving library: {}", libraryId);
		
		return libraryManagementService.retrieveLibrary(libraryId);
	}
	
	@GetMapping("/{libraryId}/book")
	public List<BookData> retrieveAllBooks(@PathVariable Long libraryId)
	{
		log.info("Retrieving all books for library: {}", libraryId);
		
		return libraryManagementService.retrieveAllBooks(libraryId);
	}
	
	@GetMapping("/{libraryId}/book/{bookId}")
	public BookData retrieveBook(@PathVariable Long libraryId, @PathVariable Long bookId)
	{
		log.info("Retrieving book: {} for library: {}", bookId, libraryId);
		
		return libraryManagementService.retrieveBook(libraryId, bookId);
	}
	
	@GetMapping("/{libraryId}/book/borrower")
	public List<BorrowerData> retrieveAllBorrowers(@PathVariable Long libraryId)
	{
		log.info("Retrieving all borrowers for library: {}", libraryId);
		
		return libraryManagementService.retrieveAllBorrowers(libraryId);
	}
	
	@GetMapping("/{libraryId}/book/borrower/{borrowerId}")
	public BorrowerData retrieveBorrower(@PathVariable Long libraryId, @PathVariable Long borrowerId)
	{
		log.info("Retrieving borrower: {} for library: {}", borrowerId, libraryId);
		
		return libraryManagementService.retrieveBorrower(libraryId, borrowerId);
	}
	
	@GetMapping("/{libraryId}/book/checkout")
	public List<CheckoutData> retrieveAllCheckouts(@PathVariable Long libraryId)
	{
		log.info("Retrieving all checkouts for library: {}", libraryId);
		
		return libraryManagementService.retrieveAllCheckouts(libraryId);
	}
	
	@GetMapping("/{libraryId}/book/checkout/{checkoutId}")
	public CheckoutData retrieveCheckout(@PathVariable Long libraryId, @PathVariable Long checkoutId)
	{
		log.info("Retrieving checkout: {} for library: {}", checkoutId, libraryId);
		
		return libraryManagementService.retrieveCheckout(libraryId, checkoutId);
	}
	
	//********************************************
	//The following methods will be used to Update
	//********************************************
	
	@PutMapping("/{libraryId}")
	public LibraryManagementData updateLibrary(@PathVariable Long libraryId ,@RequestBody LibraryManagementData libraryData)
	{
		libraryData.setLibraryId(libraryId);
		log.info("Updating library: {}", libraryData);
		
		return libraryManagementService.saveLibrary(libraryData);
	}
	@PutMapping("/{libraryId}/book/{bookId}")
	public BookData updateBook(@PathVariable Long libraryId, @PathVariable Long bookId ,@RequestBody BookData bookData)
	{
		bookData.setBookId(bookId);
		log.info("Updating book: {}", bookData);
		
		return libraryManagementService.saveBook(libraryId, bookData);
	}
	@PutMapping("/{libraryId}/book/borrower/{borrowerId}")
	public BorrowerData updateBorrower(@PathVariable Long libraryId, @PathVariable Long borrowerId ,@RequestBody BorrowerData borrowerData)
	{
		borrowerData.setBorrowerId(borrowerId);
		log.info("Updating borrower: {}", borrowerData);
		
		return libraryManagementService.saveBorrower(borrowerData);
	}
	@PutMapping("/book/{bookId}/checkout/{checkoutId}")
	public CheckoutData updateCheckout(@PathVariable Long checkoutId ,@RequestBody CheckoutData checkoutData)
	{
		checkoutData.setCheckoutId(checkoutId);
		log.info("Updating checkout: {}", checkoutData);
		
		return libraryManagementService.returnBook(checkoutId);
	}
	
	//********************************************
	//The following methods will be used to Delete
	//********************************************
	@DeleteMapping("/{libraryId}")
	public void deleteLibrary(@PathVariable Long libraryId)
	{
		log.info("Deleting library: {}", libraryId);
		
		libraryManagementService.deleteLibrary(libraryId);
	}
	@DeleteMapping("/{libraryId}/book")
	public void deleteBook(@PathVariable Long libraryId, @PathVariable Long bookId)
	{
		log.info("Deleting book: {} for library: {}", bookId, libraryId);
		
		libraryManagementService.deleteBook(libraryId, bookId);
	}
	@DeleteMapping("/{libraryId}/book/borrower")
	public void deleteBorrower(@PathVariable Long libraryId, @PathVariable Long borrowerId)
	{
		log.info("Deleting borrower: {} for library: {}", borrowerId, libraryId);
		
		libraryManagementService.deleteBorrower(libraryId, borrowerId);
	}
	@DeleteMapping("/book/{bookId}/checkout")
	public void deleteCheckout(@PathVariable Long libraryId, @PathVariable Long checkoutId)
	{
		log.info("Deleting checkout: {} for library: {}", checkoutId, libraryId);
		
		libraryManagementService.deleteCheckout(libraryId, checkoutId);
	}
	
	
	
}
