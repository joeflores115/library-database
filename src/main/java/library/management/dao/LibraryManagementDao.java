package library.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import library.management.entity.Library;

public interface LibraryManagementDao extends JpaRepository<Library, Long> {

}
