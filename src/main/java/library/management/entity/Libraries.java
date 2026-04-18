package library.management.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Libraries {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long libraryId;
	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone;
	
	// Add any additional fields or relationships as needed
	@OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Book> books = new HashSet<>();
	
}
