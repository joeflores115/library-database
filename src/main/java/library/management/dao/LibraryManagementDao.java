package library.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import library.management.entity.Libraries;

public interface LibraryManagementDao extends JpaRepository<Libraries, Long> {

}
