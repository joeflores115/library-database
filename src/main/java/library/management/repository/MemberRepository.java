package library.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import library.management.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
