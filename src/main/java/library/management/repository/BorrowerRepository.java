package library.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import library.management.entity.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
}
