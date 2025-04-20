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
import lombok.EqualsAndHashCode;

@Entity
@Data
public class Borrower {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long borrowerId;
	@EqualsAndHashCode.Exclude
	private String name;
	@EqualsAndHashCode.Exclude
	private String address;
	@EqualsAndHashCode.Exclude
	private String email;
	
	@OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, orphanRemoval = true)
	@EqualsAndHashCode.Exclude
	private Set<Checkout> checkouts = new HashSet<>();
	
}
