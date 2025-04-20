package library.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import library.management.entity.Borrower;

public interface LibraryManagementBorrowerDao extends JpaRepository<Borrower, Long> {

}
