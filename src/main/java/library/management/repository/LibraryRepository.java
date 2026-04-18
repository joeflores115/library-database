package library.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import library.management.entity.Library;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
