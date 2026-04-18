package library.management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import library.management.entity.Checkout;

public interface CheckoutRepository extends JpaRepository<Checkout, Long> {
	List<Checkout> findAllByBookLibraryLibraryId(Long libraryId);
}
