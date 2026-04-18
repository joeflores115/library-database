package library.management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import library.management.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findAllByLibraryLibraryId(Long libraryId);
}
