package library.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import library.management.dto.LoanDto;
import library.management.entity.Book;
import library.management.entity.Member;
import library.management.entity.Loan;
import library.management.exception.NoCopiesAvailableException;
import library.management.repository.BookRepository;
import library.management.repository.LoanRepository;
import library.management.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
public class SharedLibraryServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private SharedLibraryService sharedLibraryService;

    @Test
    public void testBorrowBook_Success() {
        Long bookId = 1L;
        Long memberId = 2L;

        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle("Test Book");

        Member member = new Member();
        member.setMemberId(memberId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(loanRepository.existsByBookBookIdAndReturnDateIsNull(bookId)).thenReturn(false);
        when(loanRepository.save(any(Loan.class))).thenAnswer(i -> {
            Loan l = (Loan) i.getArguments()[0];
            l.setLoanId(100L); // Ensure ID is set for DTO conversion
            return l;
        });

        LoanDto request = new LoanDto();
        request.setBookId(bookId);
        request.setBorrowerId(memberId);

        LoanDto result = sharedLibraryService.borrowBook(request);

        assertNotNull(result);
        assertEquals(bookId, result.getBookId());
        assertEquals(memberId, result.getBorrowerId());
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    public void testBorrowBook_AlreadyLent() {
        Long bookId = 1L;
        Long memberId = 2L;

        Book book = new Book();
        book.setBookId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));
        when(loanRepository.existsByBookBookIdAndReturnDateIsNull(bookId)).thenReturn(true);

        LoanDto request = new LoanDto();
        request.setBookId(bookId);
        request.setBorrowerId(memberId);

        assertThrows(NoCopiesAvailableException.class, () -> {
            sharedLibraryService.borrowBook(request);
        });
    }
}
