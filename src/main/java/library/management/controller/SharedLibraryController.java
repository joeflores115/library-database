package library.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import library.management.dto.BookDto;
import library.management.dto.LoanDto;
import library.management.dto.MemberDto;
import library.management.service.SharedLibraryService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class SharedLibraryController {

    @Autowired
    private SharedLibraryService sharedLibraryService;

    // --- Members ---
    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberDto createMember(@Valid @RequestBody MemberDto memberDto) {
        log.info("Creating member: {}", memberDto.getName());
        return sharedLibraryService.createMember(memberDto);
    }

    @GetMapping("/members")
    public List<MemberDto> listMembers() {
        return sharedLibraryService.listMembers();
    }

    @GetMapping("/members/{memberId}/loans")
    public List<LoanDto> listMemberLoans(@PathVariable Long memberId) {
        return sharedLibraryService.listMemberLoans(memberId);
    }

    // --- Books ---
    @PostMapping("/books")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@Valid @RequestBody BookDto bookDto) {
        log.info("Adding book: {}", bookDto.getTitle());
        return sharedLibraryService.createBook(bookDto);
    }

    @GetMapping("/books")
    public Page<BookDto> listAllBooks(Pageable pageable) {
        return sharedLibraryService.listAllBooks(pageable);
    }

    @GetMapping("/books/available")
    public List<BookDto> listAvailableBooks() {
        return sharedLibraryService.listAvailableBooks();
    }

    // --- Loans ---
    @PostMapping("/loans")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanDto borrowBook(@Valid @RequestBody LoanDto loanDto) {
        log.info("Processing loan for bookId: {} by memberId: {}", loanDto.getBookId(), loanDto.getBorrowerId());
        return sharedLibraryService.borrowBook(loanDto);
    }

    @PutMapping("/loans/{loanId}/return")
    public LoanDto returnBook(@PathVariable Long loanId) {
        log.info("Processing return for loanId: {}", loanId);
        return sharedLibraryService.returnBook(loanId);
    }

    @GetMapping("/loans/active")
    public List<LoanDto> listActiveLoans() {
        return sharedLibraryService.listActiveLoans();
    }
}
