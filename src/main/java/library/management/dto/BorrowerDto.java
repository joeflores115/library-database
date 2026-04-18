package library.management.dto;

import library.management.entity.Borrower;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BorrowerDto {
	private Long borrowerId;
	@NotBlank(message = "Name is required")
	private String name;
	@NotBlank(message = "Address is required")
	private String address;
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	public BorrowerDto(Borrower borrower) {
		this.borrowerId = borrower.getBorrowerId();
		this.name = borrower.getName();
		this.address = borrower.getAddress();
		this.email = borrower.getEmail();
	}
}
