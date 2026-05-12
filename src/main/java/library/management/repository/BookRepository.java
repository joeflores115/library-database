package library.management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import library.management.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b NOT IN (SELECT l.book FROM Loan l WHERE l.returnDate IS NULL)")
    List<Book> findAvailableBooks();
}
