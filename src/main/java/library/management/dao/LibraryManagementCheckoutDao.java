package library.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import library.management.entity.Checkout;

public interface LibraryManagementCheckoutDao extends JpaRepository<Checkout, Long> {

	List<Checkout> findAllByBookLibraryLibraryId(Long libraryId);

}
