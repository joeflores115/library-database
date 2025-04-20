package library.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import library.management.entity.Book;

public interface LibraryManagementBookDao extends JpaRepository<Book, Long> {

}
