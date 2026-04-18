package library.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import library.management.entity.Book;

public interface LibraryManagementBookDao extends JpaRepository<Book, Long> {

	List<Book> findAllByLibraryLibraryId(Long libraryId);

	List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

}
