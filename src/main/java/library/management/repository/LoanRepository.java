package library.management.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import library.management.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReturnDateIsNull();
    List<Loan> findByBorrowerMemberIdAndReturnDateIsNull(Long memberId);
    boolean existsByBookBookIdAndReturnDateIsNull(Long bookId);
}
