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

import library.management.controller.error.GlobalErrorHandler.NoCopiesAvailableException;
import library.management.controller.model.LibraryManagementData.BookData;
import library.management.controller.model.LibraryManagementData.CheckoutData;
import library.management.dao.LibraryManagementBookDao;
import library.management.dao.LibraryManagementBorrowerDao;
import library.management.dao.LibraryManagementCheckoutDao;
import library.management.dao.LibraryManagementDao;
import library.management.entity.Book;
import library.management.entity.Borrower;
import library.management.entity.Checkout;

@ExtendWith(MockitoExtension.class)
public class LibraryManagementServiceTest {

    @Mock
    private LibraryManagementBookDao bookDao;

    @Mock
    private LibraryManagementBorrowerDao borrowerDao;

    @Mock
    private LibraryManagementCheckoutDao checkoutDao;

    @Mock
    private LibraryManagementDao libraryManagementDao;

    @InjectMocks
    private LibraryManagementService libraryManagementService;

    @Test
    public void testSaveCheckout_Success() {
        Long bookId = 1L;
        Long borrowerId = 2L;
        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle("Test Book");
        book.setQuantity(5);

        Borrower borrower = new Borrower();
        borrower.setBorrowerId(borrowerId);

        when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowerDao.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(checkoutDao.save(any(Checkout.class))).thenAnswer(i -> i.getArguments()[0]);

        CheckoutData result = libraryManagementService.saveCheckout(bookId, borrowerId, new CheckoutData());

        assertNotNull(result);
        assertEquals(4, book.getQuantity());
        verify(bookDao).save(book);
        verify(checkoutDao).save(any(Checkout.class));
    }

    @Test
    public void testSaveCheckout_NoCopies() {
        Long bookId = 1L;
        Long borrowerId = 2L;
        Book book = new Book();
        book.setBookId(bookId);
        book.setQuantity(0);

        when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
        when(borrowerDao.findById(borrowerId)).thenReturn(Optional.of(new Borrower()));

        assertThrows(NoCopiesAvailableException.class, () -> {
            libraryManagementService.saveCheckout(bookId, borrowerId, new CheckoutData());
        });
    }
}
