package library.management.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
public class Checkout {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long checkoutId;
	
	@ManyToOne
	@JoinColumn(name = "book_id", nullable = false)
	@EqualsAndHashCode.Exclude
	private Book book;
	
	@ManyToOne
	@JoinColumn(name = "borrower_id", nullable = false)
	@EqualsAndHashCode.Exclude
	private Borrower borrower;
	
	@EqualsAndHashCode.Exclude
	private LocalDate checkoutDate;
	@EqualsAndHashCode.Exclude
	private LocalDate dueDate;
	@EqualsAndHashCode.Exclude
	private LocalDate returnDate;	
	
	
	
	
}
