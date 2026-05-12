package library.management.dto;

import library.management.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberDto {
	private Long memberId;
	@NotBlank(message = "Name is required")
	private String name;
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

	public MemberDto(Member member) {
		this.memberId = member.getMemberId();
		this.name = member.getName();
		this.email = member.getEmail();
	}
}
