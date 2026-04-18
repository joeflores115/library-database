package library.management.controller;

import jakarta.validation.Valid;

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

import library.management.dto.LibraryDto;
import library.management.dto.BookDto;
import library.management.dto.BorrowerDto;
import library.management.dto.CheckoutDto;
import library.management.service.LibraryManagementService;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/libraries")
@RestController
@Slf4j
public class LibraryManagementController
{
	@Autowired
	LibraryManagementService libraryManagementService;
	
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public LibraryDto createLibrary(@Valid @RequestBody LibraryDto libraryData)
	{
		log.info("Creating library: {}", libraryData);
		
		return libraryManagementService.saveLibrary(libraryData);
	}
	
	
	
	@PostMapping("/{libraryId}/books")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BookDto insertBook(@PathVariable Long libraryId, @Valid @RequestBody BookDto bookData)
	{
		log.info("Creating book: {}", bookData);
		
		return libraryManagementService.saveBook(libraryId, bookData);
	}
	
	
	@PostMapping("/borrowers")
	@ResponseStatus(code = HttpStatus.CREATED)
	public BorrowerDto insertBorrower(@Valid @RequestBody BorrowerDto borrowerData)
	{
		log.info("Creating borrower: {}", borrowerData);
		
		return libraryManagementService.saveBorrower(borrowerData);
	}
	
	
	@PostMapping("/checkouts")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CheckoutDto bookCheckout(@Valid @RequestBody CheckoutDto checkoutRequest)
	{
		log.info("Creating checkout: {}", checkoutRequest);
		
		return libraryManagementService.saveCheckout(checkoutRequest.getBookId(), checkoutRequest.getBorrowerId());
	}
	
	
	
	
	@GetMapping
	public List<LibraryDto> retrieveAllLibraries()
	{
		log.info("Retrieving all libraries");
		
		return libraryManagementService.retrieveAllLibraries();
	}
	
	@GetMapping("/{libraryId}")
	public LibraryDto retrieveLibrary(@PathVariable Long libraryId)
	{
		log.info("Retrieving library: {}", libraryId);
		
		return libraryManagementService.retrieveLibrary(libraryId);
	}
	
	@GetMapping("/{libraryId}/books")
	public List<BookDto> retrieveAllBooks(@PathVariable Long libraryId)
	{
		log.info("Retrieving all books for library: {}", libraryId);
		
		return libraryManagementService.retrieveAllBooks(libraryId);
	}
	
	@GetMapping("/{libraryId}/books/{bookId}")
	public BookDto retrieveBook(@PathVariable Long libraryId, @PathVariable Long bookId)
	{
		log.info("Retrieving book: {} for library: {}", bookId, libraryId);
		
		return libraryManagementService.retrieveBook(libraryId, bookId);
	}
	
	@GetMapping("/borrowers")
	public List<BorrowerDto> retrieveAllBorrowers()
	{
		log.info("Retrieving all borrowers");
		
		return libraryManagementService.retrieveAllBorrowers();
	}
	
	@GetMapping("/borrowers/{borrowerId}")
	public BorrowerDto retrieveBorrower(@PathVariable Long borrowerId)
	{
		log.info("Retrieving borrower: {}", borrowerId);
		
		return libraryManagementService.retrieveBorrower(borrowerId);
	}
	
	@GetMapping("/{libraryId}/checkouts")
	public List<CheckoutDto> retrieveAllCheckouts(@PathVariable Long libraryId)
	{
		log.info("Retrieving all checkouts for library: {}", libraryId);
		
		return libraryManagementService.retrieveAllCheckouts(libraryId);
	}
	
	@GetMapping("/{libraryId}/checkouts/{checkoutId}")
	public CheckoutDto retrieveCheckout(@PathVariable Long libraryId, @PathVariable Long checkoutId)
	{
		log.info("Retrieving checkout: {} for library: {}", checkoutId, libraryId);
		
		return libraryManagementService.retrieveCheckout(libraryId, checkoutId);
	}
	
	
	@PutMapping("/{libraryId}")
	public LibraryDto updateLibrary(@PathVariable Long libraryId ,@Valid @RequestBody LibraryDto libraryData)
	{
		libraryData.setLibraryId(libraryId);
		log.info("Updating library: {}", libraryData);
		
		return libraryManagementService.saveLibrary(libraryData);
	}
	@PutMapping("/{libraryId}/books/{bookId}")
	public BookDto updateBook(@PathVariable Long libraryId, @PathVariable Long bookId ,@Valid @RequestBody BookDto bookData)
	{
		bookData.setBookId(bookId);
		log.info("Updating book: {}", bookData);
		
		return libraryManagementService.saveBook(libraryId, bookData);
	}
	@PutMapping("/borrowers/{borrowerId}")
	public BorrowerDto updateBorrower(@PathVariable Long borrowerId ,@Valid @RequestBody BorrowerDto borrowerData)
	{
		borrowerData.setBorrowerId(borrowerId);
		log.info("Updating borrower: {}", borrowerData);
		
		return libraryManagementService.saveBorrower(borrowerData);
	}
	@PutMapping("/checkouts/{checkoutId}")
	public CheckoutDto updateCheckout(@PathVariable Long checkoutId ,@Valid @RequestBody CheckoutDto checkoutData)
	{
		checkoutData.setCheckoutId(checkoutId);
		log.info("Updating checkout: {}", checkoutData);
		
		return libraryManagementService.updateCheckout(checkoutId, checkoutData);
	}
	
	
		
	
	@DeleteMapping("/{libraryId}")
	public void deleteLibrary(@PathVariable Long libraryId)
	{
		log.info("Deleting library: {}", libraryId);
		
		libraryManagementService.deleteLibrary(libraryId);
	}
	@DeleteMapping("/{libraryId}/books/{bookId}")
	public void deleteBook(@PathVariable Long libraryId, @PathVariable Long bookId)
	{
		log.info("Deleting book: {} for library: {}", bookId, libraryId);
		
		libraryManagementService.deleteBook(libraryId, bookId);
	}
	@DeleteMapping("/borrowers/{borrowerId}")
	public void deleteBorrower(@PathVariable Long borrowerId)
	{
		log.info("Deleting borrower: {}", borrowerId);
		
		libraryManagementService.deleteBorrower(borrowerId);
	}
	@DeleteMapping("/checkouts/{checkoutId}")
	public void deleteCheckout(@PathVariable Long checkoutId)
	{
		log.info("Deleting checkout: {}", checkoutId);
		
		libraryManagementService.deleteCheckout(checkoutId);
	}
	
	
	
}
