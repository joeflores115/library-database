package library.management.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Entity
@Data
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId;
	
	@EqualsAndHashCode.Exclude
	private String title;
	@EqualsAndHashCode.Exclude
	private String author;
	@EqualsAndHashCode.Exclude
	private String isbn;
	@EqualsAndHashCode.Exclude
	private int quantity;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne
	@JoinColumn(name = "library_id", nullable = false)
	private Libraries library;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "book_borrower",
			joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "borrower_id")
			)
	private Set<Borrower> borrowers = new HashSet<>();
}
