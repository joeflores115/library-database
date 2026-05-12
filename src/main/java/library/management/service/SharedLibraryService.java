package library.management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import library.management.dto.BookDto;
import library.management.dto.LoanDto;
import library.management.dto.MemberDto;
import library.management.entity.Book;
import library.management.entity.Loan;
import library.management.entity.Member;
import library.management.exception.NoCopiesAvailableException;
import library.management.repository.BookRepository;
import library.management.repository.LoanRepository;
import library.management.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SharedLibraryService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Transactional
    public MemberDto createMember(MemberDto memberDto) {
        Member member = new Member();
        member.setName(memberDto.getName());
        member.setEmail(memberDto.getEmail());
        return new MemberDto(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public List<MemberDto> listMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookDto createBook(BookDto bookDto) {
        Member owner = memberRepository.findById(bookDto.getOwnerId())
                .orElseThrow(() -> new NoSuchElementException("Member (owner) not found with ID: " + bookDto.getOwnerId()));

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setIsbn(bookDto.getIsbn());
        book.setOwner(owner);

        return new BookDto(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    public Page<BookDto> listAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(BookDto::new);
    }

    @Transactional(readOnly = true)
    public List<BookDto> listAvailableBooks() {
        return bookRepository.findAvailableBooks().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public LoanDto borrowBook(LoanDto loanDto) {
        Book book = bookRepository.findById(loanDto.getBookId())
                .orElseThrow(() -> new NoSuchElementException("Book not found with ID: " + loanDto.getBookId()));

        Member borrower = memberRepository.findById(loanDto.getBorrowerId())
                .orElseThrow(() -> new NoSuchElementException("Member (borrower) not found with ID: " + loanDto.getBorrowerId()));

        // Rule: cannot borrow if already checked out
        boolean isBorrowed = loanRepository.existsByBookBookIdAndReturnDateIsNull(book.getBookId());

        if (isBorrowed) {
            throw new NoCopiesAvailableException("Book is already lent out: " + book.getTitle());
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBorrower(borrower);
        loan.setBorrowDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));

        return new LoanDto(loanRepository.save(loan));
    }

    @Transactional
    public LoanDto returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new NoSuchElementException("Loan not found with ID: " + loanId));

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Book already returned for this loan.");
        }

        loan.setReturnDate(LocalDate.now());
        return new LoanDto(loanRepository.save(loan));
    }

    @Transactional(readOnly = true)
    public List<LoanDto> listActiveLoans() {
        return loanRepository.findByReturnDateIsNull().stream()
                .map(LoanDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoanDto> listMemberLoans(Long memberId) {
        return loanRepository.findByBorrowerMemberIdAndReturnDateIsNull(memberId).stream()
                .map(LoanDto::new)
                .collect(Collectors.toList());
    }
}
