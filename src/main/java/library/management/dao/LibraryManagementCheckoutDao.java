package library.management.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import library.management.entity.Checkout;

public interface LibraryManagementCheckoutDao extends JpaRepository<Checkout, Long> {

}
