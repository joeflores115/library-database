package library.management.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Borrower {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long borrowerId;
	private String name;
	private String address;
	private String email;
	
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToMany(mappedBy = "borrowers")
	private Set<Book> books = new HashSet<>();
	
	
}
